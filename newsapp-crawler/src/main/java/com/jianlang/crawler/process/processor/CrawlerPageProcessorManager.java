package com.jianlang.crawler.process.processor;

import com.jianlang.crawler.helper.CrawlerHelper;
import com.jianlang.crawler.process.ProcessFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Component
public class CrawlerPageProcessorManager {
    @Autowired
    private CrawlerHelper crawlerHelper;

    @Resource
    private List<AbstractCrawlerPageProcessor> abstractCrawlerPageProcessors;

    /**
     * 初始化注入接口顺序的方法
     */
    @PostConstruct
    public void initProcessingFlow(){
        if(abstractCrawlerPageProcessors!=null && !abstractCrawlerPageProcessors.isEmpty()){
            abstractCrawlerPageProcessors.sort(new Comparator<ProcessFlow>() {
                @Override
                public int compare(ProcessFlow o1, ProcessFlow o2) {
                    return o1.getPriority() - o2.getPriority();
                }
            });
        }
    }

    /**
     * 处理数据
     * @param page
     */
    public void handle(Page page){
        String handleType = crawlerHelper.gethandleType(page.getRequest());
        String documentType = crawlerHelper.getDocumentType(page.getRequest());
        for (AbstractCrawlerPageProcessor pageProcessor : abstractCrawlerPageProcessors){
            boolean needHandleType = pageProcessor.isNeedHandleType(handleType);
            boolean needDocumentType = pageProcessor.isNeedDocumentType(documentType);
            if (needDocumentType && needHandleType){
                pageProcessor.handlePage(page);
            }
        }
    }
}
