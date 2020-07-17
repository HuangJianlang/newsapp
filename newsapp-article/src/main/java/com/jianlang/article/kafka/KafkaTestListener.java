package com.jianlang.article.kafka;

import com.jianlang.common.kafka.KafkaListener;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

public class KafkaTestListener implements KafkaListener {

    @Override
    public String topic() {
        return "topic.test";
    }

    @Override
    public void onMessage(ConsumerRecord consumerRecord, Consumer consumer) {
        System.out.println("===========Message recived=========" + consumerRecord);
    }

    @Override
    public void onMessage(Object o) {

    }
}
