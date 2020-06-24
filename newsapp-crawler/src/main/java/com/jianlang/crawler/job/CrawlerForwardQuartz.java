package com.jianlang.crawler.job;

import com.jianlang.common.quartz.AbstractJob;
import com.jianlang.crawler.manager.ProcessingFlowManager;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//同一个任务不能同时执行
@DisallowConcurrentExecution
@Log4j2
public class CrawlerForwardQuartz extends AbstractJob {

    @Autowired
    private ProcessingFlowManager processingFlowManager;

    /**
     * every 5 sec to run executeInternal
     * @return
     */
    @Override
    public String[] triggerCron() {
        //s m h (day in a month) month (day in a week)
        return new String[]{"0 0/10 * * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Forward handle starts");
        processingFlowManager.forwardHandle();
        log.info("Forward handle ends");
    }
}
