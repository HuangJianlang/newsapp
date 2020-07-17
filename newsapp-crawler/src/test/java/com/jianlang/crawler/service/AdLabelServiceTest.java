package com.jianlang.crawler.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdLabelServiceTest {
    @Autowired
    private AdLabelService adLabelService;

    @Test
    public void testGetLabelsIds(){
        String labelIds = adLabelService.getLabelIds("mockjs 前端 前端化工程");
        System.out.println(labelIds);
    }

    @Test
    public void testGetAdChannelByLabelIds(){
        Integer adChannelByLabelIds = adLabelService.getAdChannelByLabelIds("1,2,3");
        System.out.println(adChannelByLabelIds);
    }
}
