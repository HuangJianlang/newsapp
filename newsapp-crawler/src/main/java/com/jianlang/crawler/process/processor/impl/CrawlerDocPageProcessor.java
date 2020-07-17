package com.jianlang.crawler.process.processor.impl;

import com.jianlang.crawler.helper.CrawlerHelper;
import com.jianlang.crawler.process.entity.CrawlerConfigProperty;
import com.jianlang.crawler.process.processor.AbstractCrawlerPageProcessor;
import com.jianlang.crawler.utils.ParseRuleUtils;
import com.jianlang.model.crawler.core.parse.ParseRule;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.List;


@Component
@Log4j2
public class CrawlerDocPageProcessor extends AbstractCrawlerPageProcessor {

    @Autowired
    private CrawlerConfigProperty crawlerConfigProperty;
    @Autowired
    private CrawlerHelper crawlerHelper;

    /**
     * 处理detail
     * @param page
     */
    @Override
    public void handlePage(Page page) {
        long currentTime = System.currentTimeMillis();
        String handleType = crawlerHelper.getHandleType(page.getRequest());
        log.info("Parsing article detail page starts, url: {}, handleType: ", page.getUrl(), handleType);
        //目标页面就要使用target rules
        List<ParseRule> targetParseRuleList = crawlerConfigProperty.getTargetParseRuleList();
        //抽取有效数据
        targetParseRuleList = ParseRuleUtils.parseHtmlByRuleList(page.getHtml(), targetParseRuleList);
        if(targetParseRuleList!=null && ! targetParseRuleList.isEmpty()){
            for (ParseRule parseRule: targetParseRuleList){
                //将数据添加到配置中, 交给后续的pipeline中
                //log.info("Add data field to page, url:{}, handleType:{},  field:{}",page.getUrl(), handleType, parseRule.getField());
                page.putField(parseRule.getField(), parseRule.getMergeContent());
            }
        }
        log.info("Parsing Target page complete, url:{}, time", page.getUrl(), System.currentTimeMillis()-currentTime);
    }

    @Override
    public boolean isNeedHandleType(String handleType) {
        return CrawlerEnum.handleType.FORWARD.name().equals(handleType);
    }

    @Override
    public boolean isNeedDocumentType(String documentType) {
        return CrawlerEnum.DocumentType.PAGE.name().equals(documentType);
    }

    @Override
    public int getPriority() {
        return 120;
    }
}
