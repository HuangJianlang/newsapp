package com.jianlang.crawler.test;

import com.jianlang.crawler.manager.ProcessingFlowManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProcessingFlowManagerTest {
    @Autowired
    private ProcessingFlowManager processingFlowManager;

    @Test
    public void test(){
        processingFlowManager.handle();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
