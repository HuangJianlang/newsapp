package com.jianlang.crawler.service;

import com.jianlang.model.crawler.pojos.ClNewsAdditional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CrawlerNewsAdditionalServiceTest {
    @Autowired
    private CrawlerNewsAdditionalService crawlerNewsAdditionalService;

    @Test
    public void testSave(){
        ClNewsAdditional clNewsAdditional = new ClNewsAdditional();
        clNewsAdditional.setUrl("1235.com");
        clNewsAdditional.setCollection(3);
        clNewsAdditional.setCreatedTime(new Date());
        clNewsAdditional.setComment(10);
        crawlerNewsAdditionalService.saveAdditional(clNewsAdditional);
    }

    @Test
    public void testQueryList(){
        ClNewsAdditional clNewsAdditional = new ClNewsAdditional();
        clNewsAdditional.setUrl("1235.com");
        List<ClNewsAdditional> clNewsAdditionals = crawlerNewsAdditionalService.queryList(clNewsAdditional);
        System.out.println(clNewsAdditionals);
    }

    @Test
    public void testCheckExist(){
        System.out.println(crawlerNewsAdditionalService.checkExist("135.com"));
    }
}
