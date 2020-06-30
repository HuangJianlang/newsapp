package com.jianlang.crawler.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jianlang.common.common.contants.ESIndexConstants;
import com.jianlang.common.common.pojo.EsIndexEntity;
import com.jianlang.crawler.service.AdLabelService;
import com.jianlang.crawler.service.CrawlerNewsService;
import com.jianlang.model.admin.pojos.AdChannel;
import com.jianlang.model.article.pojos.*;
import com.jianlang.model.crawler.core.parse.ZipUtils;
import com.jianlang.model.crawler.pojos.ClNews;
import com.jianlang.model.crawler.pojos.ClNewsAdditional;
import com.jianlang.model.mappers.admin.AdChannelLabelMapper;
import com.jianlang.model.mappers.admin.AdChannelMapper;
import com.jianlang.model.mappers.app.*;
import com.jianlang.model.mappers.crawlers.ClNewsAdditionalMapper;
import com.jianlang.model.mappers.crawlers.ClNewsMapper;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@SuppressWarnings("all")
public class CrawlerNewsServiceImpl implements CrawlerNewsService {

    @Autowired
    private ClNewsMapper clNewsMapper;
    @Autowired
    private ClNewsAdditionalMapper clNewsAdditionalMapper;
    @Autowired
    private AdChannelMapper adChannelMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApArticleLabelMapper apArticleLabelMapper;
    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;
    @Autowired
    private ApArticleContentMapper apArticleContentMapper;
    @Autowired
    private AdLabelService adLabelService;
    @Autowired
    private JestClient jestClient;


    @Override
    public void saveNews(ClNews clNews) {
        clNewsMapper.insertSelective(clNews);
    }

    @Override
    public void updateNews(ClNews clNews) {
        clNewsMapper.updateByPrimaryKey(clNews);
    }

    @Override
    public void deleteByUrl(String url) {
        clNewsMapper.deleteByUrl(url);
    }

    @Override
    public List<ClNews> queryList(ClNews clNews) {
        return clNewsMapper.selectList(clNews);
    }

    //Save news to articles
    @Override
    public void saveNewsAsArticle() {
        ClNews clNews = new ClNews();
        clNews.setStatus((byte)1);
        List<ClNews> clNewsList = clNewsMapper.selectList(clNews);
        if (clNewsList != null && !clNewsList.isEmpty()){
            for (ClNews news : clNewsList){
                saveNewsAsArticle(news);
            }
        }
    }

    @Override
    public void saveNewsAsArticle(ClNews clNews) {
        String content = ZipUtils.gunzip(clNews.getContent());
        String title = clNews.getTitle();
        if (content == null || title == null){
            return;
        }
        // TODO: 6/24/20 计算图文匹配度
        List<String> images = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray jsonArray = JSON.parseArray(content);
        //parse images data
        handleTextAndImages(images, stringBuilder, jsonArray);
        Integer channelId = clNews.getChannelId();
        String channelName = "";
        if (channelId != null){
            AdChannel adChannel = adChannelMapper.selectByPrimaryKey(channelId);
            if (adChannel != null){
                channelName = adChannel.getName();
            }
        }
        log.info("Save author");
        ApAuthor author = saveAuthor(clNews);

        log.info("Save Article");
        ApArticle article = saveArticleFromCrawler(images, channelId, channelName, author.getId(), clNews);
        saveArticleLabel(article);
        ApArticleConfig articleConfig = saveArticleConfig(article);
        saveArticleContent(clNews.getContent(), article);

        log.info("Creating index");
        try {
            createEsIndex(article, content, title, channelId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Update status of news");
        updateClNewsSuccess(clNews);
    }

    private void updateClNewsSuccess(ClNews clNews) {
        clNews.setStatus((byte) 9);
        clNewsMapper.updateStatus(clNews);
    }

    private void createEsIndex(ApArticle article, String content, String title, Integer channelId) throws IOException {
        EsIndexEntity esIndexEntity = saveEsIndexEntityByCrawler(content, title, channelId, article);
        Index.Builder builder = new Index.Builder(esIndexEntity);
        builder.id(article.getId().toString());
        builder.refresh(true);
        Index index = builder.index(ESIndexConstants.ARTICLE_INDEX).type(ESIndexConstants.DEFAULT_DOC).build();
        JestResult result = jestClient.execute(index);
        if (result != null && !result.isSucceeded()) {
            throw new RuntimeException(result.getErrorMessage() + "Creating index fails!");
        }
    }

    private EsIndexEntity saveEsIndexEntityByCrawler(String content, String title, Integer channelId, ApArticle article) {
        EsIndexEntity esIndexEntity = new EsIndexEntity();
        esIndexEntity.setId(new Long(article.getId()));
        if (channelId != null){
            esIndexEntity.setChannelId(new Long(channelId));
        }
        esIndexEntity.setContent(content);
        esIndexEntity.setPublishTime(new Date());
        esIndexEntity.setStatus(new Long(1));
        esIndexEntity.setTitle(title);
        esIndexEntity.setTag("article");
        return esIndexEntity;
    }

    private void saveArticleContent(String content, ApArticle article) {
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(article.getId());
        apArticleContent.setContent(content);
        apArticleContentMapper.insert(apArticleContent);
    }

    private ApArticleConfig saveArticleConfig(ApArticle article) {
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(article.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsDelete(false);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsForward(true);
        apArticleConfigMapper.insert(apArticleConfig);
        return apArticleConfig;
    }

    private void saveArticleLabel(ApArticle article) {
        if(article != null && StringUtils.isNotEmpty(article.getLabels())){
            String labelIds = adLabelService.getLabelIds(article.getLabels());
            String[] labelIdArray = labelIds.split(",");
            for (String labelId : labelIdArray){
                ApArticleLabel tmp = new ApArticleLabel(article.getId(), Integer.parseInt(labelId));
                List<ApArticleLabel> apArticleLabelList = apArticleLabelMapper.selectList(tmp);
                if (apArticleLabelList != null && !apArticleLabelList.isEmpty()){
                    ApArticleLabel apArticleLabel = apArticleLabelList.get(0);
                    apArticleLabel.setCount(apArticleLabel.getCount() + 1);
                    apArticleLabelMapper.updateByPrimaryKeySelective(apArticleLabel);
                } else {
                    tmp.setCount(1);
                    apArticleLabelMapper.insertSelective(tmp);
                }
            }
        }
    }

    private ApArticle saveArticleFromCrawler(List<String> images, Integer channelId, String channelName, Integer id, ClNews clNews) {
        ClNewsAdditional clNewsAdditional = clNewsAdditionalMapper.selectByNewsId(clNews.getId());
        ApArticle article = new ApArticle();
        article.setChannelId(channelId);
        article.setChannelName(channelName);
        article.setAuthorName(clNews.getName());
        article.setCreatedTime(clNews.getCreatedTime());
        StringBuilder sb = new StringBuilder();
        Short layout = 0;
        for (int i = 0; i<images.size() && i < 3; i++) {
            if (i != 0) {
                sb.append(",");
            }
            layout++;
            sb.append(images.get(i));
        }
        article.setImages(sb.toString());
        article.setLabels(clNews.getLabels());
        article.setTitle(clNews.getTitle());
        article.setPublishTime(new Date());
        article.setAuthorId(new Long(id));
        article.setLayout(layout);
        article.setLikes(clNewsAdditional.getLikes());
        article.setViews(clNewsAdditional.getReadCount());
        article.setCollection(clNewsAdditional.getCollection());
        apArticleMapper.insert(article);
        return article;
    }

    private ApAuthor saveAuthor(ClNews clNews) {
        ApAuthor author = apAuthorMapper.selectByAuthorName(clNews.getName());
        if (author == null || author.getId() == null){
            author = new ApAuthor();
            author.setCreatedTime(clNews.getCreatedTime());
            author.setName(clNews.getName());
            author.setType(2);
            apAuthorMapper.insert(author);
        }
        return author;
    }



    private void handleTextAndImages(List<String> images, StringBuilder stringBuilder, JSONArray jsonArray) {
        for(Object obj : jsonArray){
            JSONObject jsonObj = (JSONObject) obj;
            String type = (String) jsonObj.get("type");
            if (type.equals("image")){
                String value = (String) jsonObj.get("value");
                images.add(value);
            }
            if (type.equals("text")){
                stringBuilder.append(jsonObj.get("value"));
            }
        }
    }

    @Override
    public void saveNewsAsArticle(Integer id) {
        ClNews param = new ClNews();
        param.setId(id);
        param.setStatus((byte) 1);
        ClNews clNews = clNewsMapper.selectByIdAndStatus(param);
        if (null != clNews) {
            saveNewsAsArticle(clNews);
        }
    }


}
