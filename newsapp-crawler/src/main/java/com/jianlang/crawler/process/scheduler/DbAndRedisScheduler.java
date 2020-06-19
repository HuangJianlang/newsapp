package com.jianlang.crawler.process.scheduler;

import com.jianlang.crawler.helper.CrawlerHelper;
import com.jianlang.crawler.process.ProcessFlow;
import com.jianlang.crawler.process.entity.ProcessFlowData;
import com.jianlang.crawler.service.CrawlerNewsAdditionalService;
import com.jianlang.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@Log4j2
public class DbAndRedisScheduler extends RedisScheduler implements ProcessFlow {

    @Autowired
    private CrawlerNewsAdditionalService crawlerNewsAdditionalService;
    @Autowired
    private CrawlerHelper crawlerHelper;

    public DbAndRedisScheduler(String host) {
        super(host);
    }

    public DbAndRedisScheduler(JedisPool pool){
        super(pool);
    }

    /**
     * 判断是否重复
     * @param request
     * @param task
     * @return
     */
    @Override
    public boolean isDuplicate(Request request, Task task) {
        //只有在正向的时候才需要排重
        boolean isExist = false;
        String handleType = crawlerHelper.gethandleType(request);
        if (CrawlerEnum.handleType.FORWARD.name().equals(handleType)){
            log.info("Checking duplicate, url:{}", request.getUrl());
            //redis 排重
            isExist = super.isDuplicate(request, task);
            //数据库排重
            if (isExist == false){
                isExist = crawlerNewsAdditionalService.isExistsUrl(request.getUrl());
            }
            log.info("Checking duplicate complete, url:{}, isDuplicate:{}", request.getUrl(), isExist);
        }else {
            log.info("Reverse type");
        }
        return isExist;
    }

    @Override
    public void handle(ProcessFlowData processFlowData) {

    }

    @Override
    public CrawlerEnum.ComponentType getComponentType() {
        return CrawlerEnum.ComponentType.SCHEDULER;
    }

    @Override
    public int getPriority() {
        return 123;
    }
}
