package com.jianlang.common.kafka.messages.behavior;

import com.jianlang.common.kafka.KafkaMessage;
import com.jianlang.model.behavior.pojos.ApLikesBehavior;

public class UserLikesMessage extends KafkaMessage<ApLikesBehavior> {

    public UserLikesMessage(){

    }

    public UserLikesMessage(ApLikesBehavior behavior){
        super(behavior);
    }


    @Override
    public String getType() {
        return "user-likes";
    }
}
