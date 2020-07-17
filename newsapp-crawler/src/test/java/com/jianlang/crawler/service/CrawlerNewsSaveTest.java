package com.jianlang.crawler.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CrawlerNewsSaveTest {

    @Autowired
    private CrawlerNewsService crawlerNewsService;

    @Test
    public void testSaveArticle(){
        //crawlerNewsService.saveNewsAsArticle();
    }
}
