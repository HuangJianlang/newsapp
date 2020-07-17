package com.jianlang.crawler.process.parse.impl;

import com.alibaba.fastjson.JSON;
import com.jianlang.common.common.util.HMStringUtils;
import com.jianlang.crawler.process.parse.AbstractHtmlParsePipeline;
import com.jianlang.crawler.process.thread.CrawlerThreadPool;
import com.jianlang.crawler.service.AdLabelService;
import com.jianlang.crawler.service.CrawlerNewsAdditionalService;
import com.jianlang.crawler.service.CrawlerNewsService;
import com.jianlang.crawler.utils.DateUtils;
import com.jianlang.crawler.utils.HtmlParser;
import com.jianlang.model.admin.pojos.AdLabel;
import com.jianlang.model.crawler.core.label.HtmlLabel;
import com.jianlang.model.crawler.core.parse.ZipUtils;
import com.jianlang.model.crawler.core.parse.impl.CrawlerParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import com.jianlang.model.crawler.pojos.ClNews;
import com.jianlang.model.crawler.pojos.ClNewsAdditional;
import com.jianlang.model.mappers.admin.AdChannelMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class CrawlerHtmlParsePipeline extends AbstractHtmlParsePipeline<CrawlerParseItem> {

    @Autowired
    private CrawlerNewsAdditionalService crawlerNewsAdditionalService;
    @Autowired
    private CrawlerNewsService crawlerNewsService;
    @Autowired
    private AdLabelService adLabelService;

    @Value("${crawler.nextupdatehours}")
    private String nextUpdateHours;

    /**
     * 数据处理入口
     * @param parseItem
     */
    @Override
    public void handleHtmlData(CrawlerParseItem parseItem) {
        log.info("Putting Data into thread pool, url:{}, handleType:{}", parseItem.getUrl(), parseItem.getHandleType());
        CrawlerThreadPool.submit(() -> {
            //Forward -> add data
            if (CrawlerEnum.handleType.FORWARD.name().equals(parseItem.getHandleType())){
                log.info("Handling parse item starts, url:{}, handleType:{}", parseItem.getUrl(), parseItem.getHandleType());
                addParseItem(parseItem);
            }else if (CrawlerEnum.handleType.REVERSE.name().equals(parseItem.getHandleType())){
                updateAdditional(parseItem);
            }
        });
        log.info("Handling article data ends.");
    }

    /**
     * 阅读次数处理：阅读数 200 -> 200
     * @param itemsAll
     */
    @Override
    public void preParameterHandle(Map<String, Object> itemsAll) {
        String readCount = HMStringUtils.toString(itemsAll.get("readCount"));
        if(StringUtils.isNotEmpty(readCount)){
            readCount = readCount.split(" ")[0];
            if(StringUtils.isNotEmpty(readCount)){
                itemsAll.put("readCount", readCount);
            }
        }
    }

    @Override
    public int getPriority() {
        return 140;
    }

    /**
     * Reverse update
     * @param parseItem
     */
    private void updateAdditional(CrawlerParseItem parseItem) {
        log.info("Updating additional data");
        if (parseItem != null){
            ClNewsAdditional clNewsAdditional = crawlerNewsAdditionalService.getAdditionalByUrl(parseItem.getUrl());
            if (clNewsAdditional != null){
                clNewsAdditional.setNewsId(null);
                clNewsAdditional.setUrl(null);
                //阅读量设置
                clNewsAdditional.setReadCount(parseItem.getReadCount());
                //评论数设置
                clNewsAdditional.setComment(parseItem.getCommentCount());
                //点赞数设置
                clNewsAdditional.setLikes(parseItem.getLikes());
                //更新数据设置
                clNewsAdditional.setUpdatedTime(new Date());
                clNewsAdditional.setUpdateNum(clNewsAdditional.getUpdateNum() + 1);
                int nextUpdateHours = getNextUpdateHours(clNewsAdditional.getUpdateNum());
                clNewsAdditional.setNextUpdateTime(DateUtils.addHours(new Date(), nextUpdateHours));
                crawlerNewsAdditionalService.updateAdditional(clNewsAdditional);
            }
        }
    }

    //private function

    /**
     * Forward save article
     * @param parseItem
     */
    private void addParseItem(CrawlerParseItem parseItem) {
        String url = null;
        String handleType = null;
        if (parseItem != null){
            url = parseItem.getUrl();
            handleType = parseItem.getHandleType();
            log.info("Adding data begin: url:{}, handleType:{}", url, handleType);
            //add article data
            ClNews clNews = addClNewsData(parseItem);
            if ( clNews != null){
                //添加附加信息
                addAdditionalData(parseItem, clNews);
                // TODO: 6/19/20 添加评论数据
            }
            // TODO: 6/19/20 kafka 发送消息审核文章
            crawlerNewsService.saveNewsAsArticle();

        }
        log.info("Adding data begin: url:{}", url);
    }

    /**
     * 文章附加数据处理: 例如阅读数，点赞数等等
     * @param parseItem
     * @param clNews 文章的主要信息 如标题，正文 作者等
     */
    private void addAdditionalData(CrawlerParseItem parseItem, ClNews clNews) {
        if (parseItem != null && clNews != null){
            ClNewsAdditional clNewsAdditional = toClNewsAddtional(parseItem, clNews);
            crawlerNewsAdditionalService.saveAdditional(clNewsAdditional);
        }
    }

    private ClNews addClNewsData(CrawlerParseItem parseItem) {
        ClNews clNews = null;
        if (parseItem != null){
            //内容要解析： Html文本 -> 固定格式保存
            HtmlParser htmlParser = HtmlParser.getHtmlParser(getParseExpression(), getDefHtmlStyleMap());
            List<HtmlLabel> htmlLabels = htmlParser.parseHtml(parseItem.getContent());
            int type = getDocType(htmlLabels);
            parseItem.setDocType(type); //0: no image 1: 1 image 2: more than 1

            String jsonStr = JSON.toJSONString(htmlLabels);
            parseItem.setCompressContent(ZipUtils.gzip(jsonStr));

            //添加文章
            ClNewsAdditional additionalByUrl = crawlerNewsAdditionalService.getAdditionalByUrl(parseItem.getUrl());
            if (additionalByUrl == null){
                clNews = toClNews(parseItem);
                crawlerNewsService.saveNews(clNews);
            } else {
                log.info("Article already in database, add fails. url:{}", additionalByUrl.getUrl());
            }
        }
        return clNews;
    }

    private ClNews toClNews(CrawlerParseItem parseItem) {
        ClNews clNews = new ClNews();
        clNews.setName(parseItem.getAuthor());
        clNews.setLabels(parseItem.getLabels());
        clNews.setContent(parseItem.getCompressContent());
        clNews.setLabelIds(adLabelService.getLabelIds(parseItem.getLabels()));
        //Integer adChannelByLabelId = adLabelService.getAdChannelByLabelIds(clNews.getLabelIds());
        clNews.setChannelId(adLabelService.getAdChannelId(parseItem.getChannelName().toLowerCase()));
        clNews.setTitle(parseItem.getTitle());
        clNews.setType(parseItem.getDocType());
        clNews.setStatus((byte)1);
        clNews.setCreatedTime(new Date());
        String releaseDate = parseItem.getReleaseDate();
        if (StringUtils.isNotEmpty(releaseDate)){
            clNews.setOriginalTime(DateUtils.stringToDate(releaseDate, DateUtils.DATE_TIME_FORMAT));
        }
        return clNews;
    }


    private ClNewsAdditional toClNewsAddtional(CrawlerParseItem parseItem, ClNews clNews) {
        ClNewsAdditional clNewsAdditional = null;
        if (parseItem != null){
            clNewsAdditional = new ClNewsAdditional();
            clNewsAdditional.setNewsId(clNews.getId());
            clNewsAdditional.setReadCount(parseItem.getReadCount());
            clNewsAdditional.setLikes(parseItem.getLikes());
            clNewsAdditional.setComment(parseItem.getCommentCount());
            clNewsAdditional.setUrl(parseItem.getUrl());
            clNewsAdditional.setUpdatedTime(new Date());
            clNewsAdditional.setCreatedTime(new Date());
            clNewsAdditional.setUpdateNum(0);
            int nextUpdateHour = getNextUpdateHours(clNewsAdditional.getUpdateNum());
            clNewsAdditional.setNextUpdateTime(DateUtils.addHours(new Date(), nextUpdateHour));
        }
        return clNewsAdditional;
    }

    /**
     * 图文类型
     * @param htmlLabels
     * @return
     */
    private int getDocType(List<HtmlLabel> htmlLabels){
        int type = 0;
        int num = 0;
        if(htmlLabels != null && ! htmlLabels.isEmpty()){
            for (HtmlLabel htmlLabel : htmlLabels){
                if (htmlLabel.getType().equals(CrawlerEnum.HtmlType.IMG_TAG)){
                    num ++;
                }
            }
        }
        if (num == 0){
            type = 0;
        }else if (num == 1){
            type = 1;
        } else {
          type = 2;
        }
        return type;
    }

    private int getNextUpdateHours(Integer updateNum){
        if (StringUtils.isNotEmpty(nextUpdateHours)){
            String[] updateArray = nextUpdateHours.split(",");
            return Integer.parseInt(updateArray[updateNum]);
        }
        return 2 << updateNum;
    }

}
