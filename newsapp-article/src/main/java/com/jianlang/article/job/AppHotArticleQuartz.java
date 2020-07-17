package com.jianlang.article.job;

import com.jianlang.article.service.ApHotArticleService;
import com.jianlang.common.quartz.AbstractJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
@Log4j2
@DisallowConcurrentExecution
public class AppHotArticleQuartz extends AbstractJob {

    @Autowired
    private ApHotArticleService apHotArticleService;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0 0 ? * WED-FRI"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Computing Hot Article starts");
        apHotArticleService.computeHotArticle();
        log.info("Computing Hot Article ends");
    }

}
