package com.jianlang.common.kafka.messages.app;

import com.jianlang.common.kafka.KafkaMessage;
import com.jianlang.model.mess.app.ArticleVisitStreamDto;

public class ArticleVisitStreamMessage extends KafkaMessage<ArticleVisitStreamDto> {

    public ArticleVisitStreamMessage(){
    }

    public ArticleVisitStreamMessage(ArticleVisitStreamDto dto){
        super(dto);
    }

    @Override
    public String getType() {
        return "article-visit-stream";
    }
}
