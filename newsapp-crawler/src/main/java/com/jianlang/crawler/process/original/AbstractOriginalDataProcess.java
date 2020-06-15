package com.jianlang.crawler.process.original;

import com.jianlang.crawler.process.AbstractProcessFlow;
import com.jianlang.crawler.process.ProcessFlow;
import com.jianlang.crawler.process.entity.ProcessFlowData;
import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;

import java.util.List;

public abstract class AbstractOriginalDataProcess extends AbstractProcessFlow {
    @Override
    public void handel(ProcessFlowData processFlowData) {

    }

    @Override
    public CrawlerEnum.ComponentType getComponentType() {
        return null;
    }

    public abstract List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData);
}
