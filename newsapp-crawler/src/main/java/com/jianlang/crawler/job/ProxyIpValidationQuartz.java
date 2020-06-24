package com.jianlang.crawler.job;

import com.jianlang.common.quartz.AbstractJob;
import com.jianlang.crawler.proxy.ProxyIpManager;
import com.jianlang.model.crawler.core.proxy.CrawlerProxyProvider;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Log4j2
public class ProxyIpValidationQuartz extends AbstractJob {

    @Autowired
    private ProxyIpManager proxyIpManager;

    @Autowired
    private CrawlerProxyProvider crawlerProxyProvider;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0 0 0/1 * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Validating Proxy Ip starts");
        proxyIpManager.validateProxyIp();
        crawlerProxyProvider.updateProxy();
        log.info("Validating Proxy Ip ends");
    }
}
