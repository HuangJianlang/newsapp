package com.jianlang.article.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jianlang.common.kafka.KafkaTopicConfig;
import com.jianlang.common.kafka.messages.UpdateArticleMessage;
import com.jianlang.common.kafka.messages.app.ArticleVisitStreamMessage;
import com.jianlang.common.kafkastream.KafkaStreamListener;
import com.jianlang.model.mess.app.ArticleVisitStreamDto;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * stream 既能接受消息，也能发送消息
 */
@Component
@Log4j2
public class HotArticleStreamHandler implements KafkaStreamListener<KStream<?, String>> {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String listenerTopic() {
        return kafkaTopicConfig.getArticleUpdateBus();
    }

    @Override
    public String sendTopic() {
        return kafkaTopicConfig.getArticleIncrHandle();
    }

    // TODO: 6/27/20 流程复杂，需要理解
    @Override
    public KStream<?, String> getService(KStream<?, String> stream) {
        return stream.map((key, val) -> {
            UpdateArticleMessage value = format(val);
            return new KeyValue<>(value.getData().getArticleId().toString(), value.getData().getType().name() + ":" + value.getData().getAdd());
        }).groupByKey().windowedBy(TimeWindows.of(10000)).aggregate(new Initializer<String>() {
            @Override
            public String apply() {
                return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
            }
        }, new Aggregator<Object, String, String>() {
            @Override
            public String apply(Object aggKey, String value, String aggValue) {
                //类似于  likes:1
                value = value.replace("UpdateArticle(", "").replace(")", "");
                String valAry[] = value.split(":");
                if ("null".equals(valAry[1])) {
                    return aggValue;
                }
                int col = 0, com = 0, lik = 0, vie = 0;
                if("LIKES".equalsIgnoreCase(valAry[0])){
                    lik+=Integer.valueOf(valAry[1]);
                }
                if("COLLECTION".equalsIgnoreCase(valAry[0])){
                    col+=Integer.valueOf(valAry[1]);
                }
                if("COMMENT".equalsIgnoreCase(valAry[0])){
                    com+=Integer.valueOf(valAry[1]);
                }
                if("VIEWS".equalsIgnoreCase(valAry[0])){
                    vie+=Integer.valueOf(valAry[1]);
                }
                return String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
            }
        }, Materialized.as("count-article-num-miukoo-1")).toStream().map((key, value) -> {
            return new KeyValue<>(key.key().toString(), formatObj(key.key().toString(), value));
        });
    }

    private UpdateArticleMessage format(String val) {
        UpdateArticleMessage msg = null;
        try {
            msg = objectMapper.readValue(val, UpdateArticleMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    private String formatObj(String articleId, String value) {
        String ret = "";
        ArticleVisitStreamMessage temp = new ArticleVisitStreamMessage();
        ArticleVisitStreamDto dto = new ArticleVisitStreamDto();
        String regEx = "COLLECTION:(\\d+),COMMENT:(\\d+),LIKES:(\\d+),VIEWS:(\\d+)";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(value);
        if (mat.find()) {
            dto.setCollect(Long.valueOf(mat.group(1)));
            dto.setCommont(Long.valueOf(mat.group(2)));
            dto.setLike(Long.valueOf(mat.group(3)));
            dto.setView(Long.valueOf(mat.group(4)));
        } else {
            dto.setCollect(0);
            dto.setCommont(0);
            dto.setLike(0);
            dto.setView(0);
        }
        dto.setArticleId(Integer.valueOf(articleId));
        temp.setData(dto);
        try {
            ret = objectMapper.writeValueAsString(temp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
