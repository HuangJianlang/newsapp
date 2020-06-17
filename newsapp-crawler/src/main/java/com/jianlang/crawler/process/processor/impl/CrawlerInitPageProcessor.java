package com.jianlang.crawler.process.processor.impl;

import com.jianlang.crawler.process.entity.CrawlerConfigProperty;
import com.jianlang.crawler.process.processor.AbstractCrawlerPageProcessor;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.List;

//初始化页面解析
@Component
public class CrawlerInitPageProcessor extends AbstractCrawlerPageProcessor {

    @Autowired
    private CrawlerConfigProperty crawlerConfigProperty;

    @Override
    public void handlePage(Page page) {
        String initCrawlerXpath = crawlerConfigProperty.getInitCrawlerXpath();
        //helper page url
        List<String> helpUrls = page.getHtml().xpath(initCrawlerXpath).links().all();
        //传给下一级
        addSpiderRequest(helpUrls, page.getRequest(), CrawlerEnum.DocumentType.HELP);
    }

    @Override
    public boolean isNeedHandleType(String handleType) {
        return CrawlerEnum.handleType.FORWARD.name().equals(handleType);
    }

    @Override
    public boolean isNeedDocumentType(String documentType) {
        return CrawlerEnum.DocumentType.INIT.name().equals(documentType);
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
