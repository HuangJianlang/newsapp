package com.jianlang.crawler.manager;

import com.jianlang.crawler.config.CrawlerConfig;
import com.jianlang.crawler.process.ProcessFlow;
import com.jianlang.crawler.process.entity.CrawlerComponent;
import com.jianlang.crawler.process.entity.ProcessFlowData;
import com.jianlang.model.crawler.core.parse.ParseItem;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Component
@Log4j2
public class ProcessingFlowManager {
    @Autowired
    private CrawlerConfig crawlerConfig;

    @Resource
    private List<ProcessFlow> processFlowList;

    /**
     * Spring 启动时初始化方法
     * 通过子类的优先级进行排序
     * 初始化spider
     */
    @PostConstruct
    public void initProcessingFlow(){
        if(processFlowList!=null && !processFlowList.isEmpty()){
            processFlowList.sort(new Comparator<ProcessFlow>() {
                @Override
                public int compare(ProcessFlow o1, ProcessFlow o2) {
                    return o1.getPriority() - o2.getPriority();
                }
            });
        }
        Spider spider = configSpider();
        crawlerConfig.setSpider(spider);
    }

    private Spider configSpider() {
        Spider spider = initSpider();
        spider.thread(5);
        return spider;
    }

    /**
     * 根据ProcessFlow接口的getComponentType接口类型生成spider对象
     * @return
     */
    private Spider initSpider(){
        Spider spider = null;
        CrawlerComponent crawlerComponent = getComponent(processFlowList);
        if (crawlerComponent != null){
            PageProcessor pageProcessor = crawlerComponent.getPageProcessor();
            if (pageProcessor != null){
                spider = Spider.create(pageProcessor);
            }
            if (spider!=null && crawlerComponent.getScheduler() != null){
                spider.setScheduler(crawlerComponent.getScheduler());
            }
            if (spider!=null && crawlerComponent.getDownloader() != null){
                spider.setDownloader(crawlerComponent.getDownloader());
            }
            List<Pipeline> pipelineList = crawlerComponent.getPipelineList();
            if (spider!=null && pipelineList!= null && !pipelineList.isEmpty()){
                for(Pipeline pipeline : pipelineList){
                    spider.addPipeline(pipeline);
                }
            }
        }
        return spider;
    }

    /**
     * 抓取组建的封装
     * @param processFlowList
     * @return
     */
    private CrawlerComponent getComponent(List<ProcessFlow> processFlowList) {
        CrawlerComponent component = new CrawlerComponent();
        for (ProcessFlow processFlow : processFlowList) {
            if (processFlow.getComponentType() == CrawlerEnum.ComponentType.PAGEPROCESSOR) {
                component.setPageProcessor((PageProcessor) processFlow);
            } else if (processFlow.getComponentType() == CrawlerEnum.ComponentType.PIPELINE) {
                component.addPipeline((Pipeline) processFlow);
            } else if (processFlow.getComponentType() == CrawlerEnum.ComponentType.DOWNLOAD) {
                component.setDownloader((Downloader) processFlow);
            } else if (processFlow.getComponentType() == CrawlerEnum.ComponentType.SCHEDULER) {
                component.setScheduler((Scheduler) processFlow);
            }
        }
        return component;
    }


    public void startTask(List<ParseItem> parseItems, CrawlerEnum.handleType handleType){
        ProcessFlowData processFlowData = new ProcessFlowData();
        processFlowData.setParseItemList(parseItems);
        processFlowData.sethandleType(handleType);
        for (ProcessFlow processFlow: processFlowList){
            processFlow.handle(processFlowData);
        }
        crawlerConfig.getSpider().start();
    }

    /**
     * 正向爬去
     */
    public void handle(){
        startTask(null, CrawlerEnum.handleType.FORWARD);
    }
}
