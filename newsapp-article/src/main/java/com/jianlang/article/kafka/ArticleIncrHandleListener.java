package com.jianlang.article.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.kafka.KafkaListener;
import com.jianlang.common.kafka.KafkaTopicConfig;
import com.jianlang.common.kafka.messages.app.ArticleVisitStreamMessage;
import com.jianlang.model.mess.app.ArticleVisitStreamDto;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class ArticleIncrHandleListener implements KafkaListener<String, String> {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private AppArticleService appArticleService;

    @Autowired
    private ObjectMapper mapper;


    @Override
    public String topic() {
        return kafkaTopicConfig.getArticleIncrHandle();
    }

    @Override
    public void onMessage(ConsumerRecord<String, String> data, Consumer<?, ?> consumer) {
        log.info("接收到的消息为:{}", data);
        String value = data.value();
        try {
            ArticleVisitStreamMessage message = mapper.readValue(value, ArticleVisitStreamMessage.class);
            ArticleVisitStreamDto dto = message.getData();
            appArticleService.updateArticleView(dto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
