package com.jianlang.article.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void test(){
        try{
            kafkaTemplate.send("topic.test", "testKey", "HelloWorld");
            System.out.println("Message sent.");
            Thread.sleep(500000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}