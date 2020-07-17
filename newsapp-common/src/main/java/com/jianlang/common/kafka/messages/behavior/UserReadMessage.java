package com.jianlang.common.kafka.messages.behavior;

import com.jianlang.common.kafka.KafkaMessage;
import com.jianlang.model.behavior.pojos.ApReadBehavior;

public class UserReadMessage extends KafkaMessage<ApReadBehavior> {

    public UserReadMessage(){
    }

    public UserReadMessage(ApReadBehavior behavior){
        super(behavior);
    }

    @Override
    public String getType() {
        return "user-read";
    }
}
