package com.jianlang.crawler.service;

import com.jianlang.model.crawler.pojos.ClIpPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CrawlerIpPoolServiceTest {
    @Autowired
    private CrawlerIpPoolService crawlerIpPoolService;

    @Test
    public void testSaveIpPool(){
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setIp("1111.2222.3333");
        clIpPool.setPort(12345);
        clIpPool.setEnable(true);
        clIpPool.setCreatedTime(new Date());
        crawlerIpPoolService.saveCrawlerIpPool(clIpPool);
    }

    @Test
    public void testCheckExist(){
        System.out.println(crawlerIpPoolService.checkExist("1111.2222", 12345));
        System.out.println(crawlerIpPoolService.checkExist("1111", 12345));
        System.out.println(crawlerIpPoolService.checkExist("1111.2222", 1234));
    }

    @Test
    public void testDelete(){
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setId(1);
        crawlerIpPoolService.delete(clIpPool);
    }

    @Test
    public void testUpdate(){
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setId(11456);
        clIpPool.setIp("111.222.333");
        clIpPool.setPort(123);
        clIpPool.setEnable(true);
        crawlerIpPoolService.updateCrawlerIpPool(clIpPool);
    }

}
