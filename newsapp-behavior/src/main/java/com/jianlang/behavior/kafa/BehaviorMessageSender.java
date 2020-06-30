package com.jianlang.behavior.kafa;

import com.jianlang.common.kafka.KafkaMessage;
import com.jianlang.common.kafka.KafkaSender;
import com.jianlang.common.kafka.messages.UpdateArticleMessage;
import com.jianlang.common.kafka.messages.behavior.UserLikesMessage;
import com.jianlang.common.kafka.messages.behavior.UserReadMessage;
import com.jianlang.model.behavior.pojos.ApLikesBehavior;
import com.jianlang.model.mess.app.UpdateArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class BehaviorMessageSender{
    @Autowired
    private KafkaSender sender;

    /**
     * 发送点赞+1 的消息
     * @param message
     * @param apUserId
     * @param isSendToArticle
     */
    public void sendMessagePlus(KafkaMessage message, Long apUserId, boolean isSendToArticle){
        if (isSendToArticle){
            UpdateArticleMessage articleMessage = parseMessage(message, apUserId, 1);
            sender.sendArticleUpdateBus(articleMessage);
        }
    }


    public void sendMessageReduce(KafkaMessage message, Long apUserId, boolean isSendToArticle){
        if (isSendToArticle){
            UpdateArticleMessage articleMessage = parseMessage(message, apUserId, -1);
            sender.sendArticleUpdateBus(articleMessage);
        }
    }

    private UpdateArticleMessage parseMessage(KafkaMessage message, Long apUserId, int i) {
        UpdateArticle updateArticle = new UpdateArticle();
        if (apUserId != null){
            updateArticle.setApUserId(apUserId.intValue());
        }
        if (message instanceof UserLikesMessage){
            UserLikesMessage userLikesMessage = (UserLikesMessage) message;
            if (userLikesMessage.getData().getType().equals(ApLikesBehavior.Type.ARTICLE.getCode())){
                updateArticle.setType(UpdateArticle.UpdateArticleType.LIKES);
                updateArticle.setAdd(i);
                updateArticle.setArticleId(userLikesMessage.getData().getEntryId());
                updateArticle.setBehaviorId(userLikesMessage.getData().getBehaviorEntryId());
            }
        } else if (message instanceof UserReadMessage){
            UserReadMessage userReadMessage = (UserReadMessage) message;
            updateArticle.setType(UpdateArticle.UpdateArticleType.VIEWS);
            updateArticle.setAdd(i);
            updateArticle.setArticleId(userReadMessage.getData().getArticleId());
            updateArticle.setBehaviorId(userReadMessage.getData().getEntryId());
        }

        if (updateArticle.getArticleId() != null){
            return new UpdateArticleMessage(updateArticle);
        }
        return null;
    }
}
