package com.jianlang.crawler.process.entity;

import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;

import java.util.List;

/**
 * 流程数据
 */
public class ProcessFlowData {
    /**
     * 抓取对象列表
     */
    private List<ParseItem> parseItemList;

    /**
     * 处理类型
     */
    private CrawlerEnum.handleType handleType = CrawlerEnum.handleType.FORWARD;


    public List<ParseItem> getParseItemList() {
        return parseItemList;
    }

    public void setParseItemList(List<ParseItem> parseItemList) {
        this.parseItemList = parseItemList;
    }


    public CrawlerEnum.handleType getHandleType() {
        return handleType;
    }

    public void setHandleType(CrawlerEnum.handleType handleType) {
        this.handleType = handleType;
    }

}
