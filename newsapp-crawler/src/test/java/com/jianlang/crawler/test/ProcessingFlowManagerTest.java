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
    public void testForward(){
        try {
            processingFlowManager.forwardHandle();
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testReverse(){
        //processingFlowManager.reverseHandle();
    }
}
