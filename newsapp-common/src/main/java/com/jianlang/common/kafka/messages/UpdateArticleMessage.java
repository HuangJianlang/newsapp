package com.jianlang.common.kafka.messages;

import com.jianlang.common.kafka.KafkaMessage;
import com.jianlang.model.mess.app.UpdateArticle;

public class UpdateArticleMessage extends KafkaMessage<UpdateArticle> {

    public UpdateArticleMessage(){

    }

    public UpdateArticleMessage(UpdateArticle data){
        super(data);
    }

    @Override
    public String getType() {
        return "update-article";
    }
}
