package com.jianlang.behavior.service.impl;

import com.jianlang.behavior.kafa.BehaviorMessageSender;
import com.jianlang.behavior.service.AppLikesBehaviorService;
import com.jianlang.common.kafka.messages.behavior.UserLikesMessage;
import com.jianlang.common.zookeeper.sequence.Sequences;
import com.jianlang.model.behavior.dtos.LikesBehaviorDto;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.behavior.pojos.ApLikesBehavior;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.ApLikesBehaviorMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppLikesBehaviorServiceImpl implements AppLikesBehaviorService {

    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;
    @Autowired
    private Sequences sequences;
    @Autowired
    private BehaviorMessageSender sender;


    @Override
    public ResponseResult saveLikesBehavior(LikesBehaviorDto dto) {
        //get user info and device id
        //get user id or device id get behavior entry id
        //get article ids
        //query ap_show_behavior table based on entry id and article id to filter(passages already in list)
        ApUser user = AppThreadLocalUtils.getUser();
        if(user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if(user != null){
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = appShowBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        if (apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApLikesBehavior apLikesBehavior = new ApLikesBehavior();
        apLikesBehavior.setId(sequences.sequenceApLikes());
        apLikesBehavior.setBehaviorEntryId(apBehaviorEntry.getId());
        apLikesBehavior.setCreatedTime(new Date());
        apLikesBehavior.setEntryId(dto.getEntryId());
        apLikesBehavior.setType(dto.getType());
        apLikesBehavior.setOperation(dto.getOperation());
        apLikesBehavior.setBurst("");

        int code = apLikesBehaviorMapper.insert(apLikesBehavior);
        if (code == 1){
            if (apLikesBehavior.getOperation() == ApLikesBehavior.Operation.LIKE.getCode()){
                sender.sendMessagePlus(new UserLikesMessage(apLikesBehavior), userId, true);
            } else if (apLikesBehavior.getOperation() == ApLikesBehavior.Operation.CANCEL.getCode()){
                sender.sendMessageReduce(new UserLikesMessage(apLikesBehavior), userId, true);
            }
        }
        return ResponseResult.okResult(code);
    }
}
