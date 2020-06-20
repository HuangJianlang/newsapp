package com.jianlang.crawler.process.original.impl;

import com.jianlang.crawler.config.CrawlerConfig;
import com.jianlang.crawler.process.entity.ProcessFlowData;
import com.jianlang.crawler.process.original.AbstractOriginalDataProcess;
import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.core.parse.impl.CrawlerParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//爬去初始页面
@Component
public class CsdnOriginalDataProcess extends AbstractOriginalDataProcess {

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData) {
        List<ParseItem> initParseItems = null;
        List<String> initUrls = crawlerConfig.getInitCrawlerUrlList();
        if (initUrls!=null &&  !initUrls.isEmpty()){
            initParseItems = initUrls.stream().map(url -> {
                CrawlerParseItem parseItem = new CrawlerParseItem();
                parseItem.setUrl(url + "?rnd=" + System.currentTimeMillis());
                parseItem.setDocumentType(CrawlerEnum.DocumentType.INIT.name());
                parseItem.setHandleType(CrawlerEnum.handleType.FORWARD.name());
                return parseItem;
            }).collect(Collectors.toList());
        }
        return initParseItems;
    }
}
