package com.jianlang.crawler.process.processor;

import com.jianlang.crawler.helper.CrawlerHelper;
import com.jianlang.crawler.process.AbstractProcessFlow;
import com.jianlang.crawler.process.entity.ProcessFlowData;
import com.jianlang.crawler.utils.ParseRuleUtils;
import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.core.parse.ParseRule;
import com.jianlang.model.crawler.core.parse.impl.CrawlerParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public abstract class AbstractCrawlerPageProcessor extends AbstractProcessFlow implements PageProcessor {

    @Autowired
    private CrawlerHelper crawlerHelper;

    @Autowired
    private CrawlerPageProcessorManager crawlerPageProcessorManager;

    @Override
    public void handle(ProcessFlowData processFlowData) {

    }

    @Override
    public CrawlerEnum.ComponentType getComponentType() {
        return CrawlerEnum.ComponentType.PAGEPROCESSOR;
    }

    /**
     * 定制爬虫逻辑的方法
     * 编写抽取逻辑
     * @param page
     */
    @Override
    public void process(Page page) {
        long currentTime = System.currentTimeMillis();
        String handleType = crawlerHelper.gethandleType(page.getRequest());
        log.info("parsing web site start: url:{}, handleType:{}", page.getUrl(),handleType);
        crawlerPageProcessorManager.handle(page);
        log.info("pasring web site complete: url:{}, handleType:{}, totalTime:{}", page.getUrl(), handleType, System.currentTimeMillis()-currentTime);
    }


    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(3).setRetrySleepTime(1000).setSleepTime(1000).setTimeOut(10000);
        //header 配置
        Map<String, String> headerMap = getHeaderMap();
        if (headerMap != null && ! headerMap.isEmpty()){
            for(Map.Entry<String, String> entry : headerMap.entrySet()){
                site.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return site;
    }

    /*
    解析用户空间下的 url helper列表
     */
    public List<String> getHelpUrlList(List<ParseRule> helpParseRuleList){
        List<String> helperUrlList = new ArrayList<>();
        for (ParseRule parseRule : helpParseRuleList){
            List<String> urlLinks = ParseRuleUtils.getUrlLinks(parseRule.getParseContentList());
            helperUrlList.addAll(urlLinks);
        }
        return helperUrlList;
    }

    /**
     * 发送请求, 添加数据到爬虫列表中
     */
    public void addSpiderRequest(List<String> urlList, Request request, CrawlerEnum.DocumentType documentType){
        //过程 将url -> ParseItems
        List<ParseItem> parseItemList = new ArrayList<>();
        if (urlList != null && !urlList.isEmpty()){
            for(String url : urlList){
                CrawlerParseItem crawlerParseItem = new CrawlerParseItem();
                crawlerParseItem.setUrl(url);
                crawlerParseItem.setDocumentType(documentType.name());
                crawlerParseItem.sethandleType(crawlerHelper.gethandleType(request));
                parseItemList.add(crawlerParseItem);
            }
        }
        addSpiderRequest(parseItemList);
    }

    /**
     * 处理页面
     * @param page
     */
    public abstract void handlePage(Page page);

    /**
     * 该类型是否需要处理
     * @param handleType
     * @return
     */
    public abstract boolean isNeedHandleType(String handleType);

    public abstract boolean isNeedDocumentType(String documentType);
}
