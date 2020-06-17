package com.jianlang.crawler.process.original;

import com.jianlang.crawler.process.AbstractProcessFlow;
import com.jianlang.crawler.process.ProcessFlow;
import com.jianlang.crawler.process.entity.ProcessFlowData;
import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * 初始化url前置处理器
 */
@Log4j2
public abstract class AbstractOriginalDataProcess extends AbstractProcessFlow {
    @Override
    public void handle(ProcessFlowData processFlowData) {
        List<ParseItem> initialDataList = processFlowData.getParseItemList();
        if(initialDataList == null || initialDataList.isEmpty()){
            log.info("Getting Initial URL lists...");
            initialDataList = parseOriginalRequestData(processFlowData);
            log.info("Getting Initial URL lists completes");
        }
        if (initialDataList != null && ! initialDataList.isEmpty()){
            addSpiderRequest(initialDataList);
        } else {
          log.error("Initial URL list is empty");
        }
        postprocess(processFlowData);
    }

    @Override
    public CrawlerEnum.ComponentType getComponentType() {
        return CrawlerEnum.ComponentType.NORMAL;
    }

    public abstract List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData);


    public void initialDataListhandle(List<ParseItem> initialDataList) {
    }

    public void postprocess(ProcessFlowData processFlowData) {
    }
}
