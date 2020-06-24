package com.jianlang.crawler.job;

import com.jianlang.common.quartz.AbstractJob;
import com.jianlang.crawler.service.CrawlerNewsService;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@DisallowConcurrentExecution
public class CrawlerAddArticleJob extends AbstractJob {

    @Autowired
    private CrawlerNewsService crawlerNewsService;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0 0 0/1 * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Save news to Article starts");
        crawlerNewsService.saveNewsAsArticle();
        log.info("Save news to Article ends");
    }
}
