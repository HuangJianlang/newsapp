package com.jianlang.crawler.job;

import com.jianlang.common.quartz.AbstractJob;
import com.jianlang.crawler.manager.ProcessingFlowManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class CrawlerReverseQuartz extends AbstractJob {

    @Autowired
    private ProcessingFlowManager processingFlowManager;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0 0 0/4 * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        processingFlowManager.reverseHandle();
    }
}
