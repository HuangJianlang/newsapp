package com.jianlang.behavior.service.impl;

import com.jianlang.behavior.kafa.BehaviorMessageSender;
import com.jianlang.behavior.service.AppReadBehaviorService;
import com.jianlang.common.kafka.messages.behavior.UserReadMessage;
import com.jianlang.common.zookeeper.sequence.Sequences;
import com.jianlang.model.behavior.dtos.ReadBehaviorDto;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.behavior.pojos.ApReadBehavior;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.ApReadBehaviorMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppReadBehaviorServiceImpl implements AppReadBehaviorService {

    @Autowired
    private Sequences sequences;
    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApReadBehaviorMapper apReadBehaviorMapper;
    @Autowired
    private BehaviorMessageSender sender;
    
    @Override
    public ResponseResult saveReadBehavior(ReadBehaviorDto dto) {
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

        ApReadBehavior apReadBehavior = apReadBehaviorMapper.selectByEntryId("", apBehaviorEntry.getId(), dto.getArticleId());
        boolean isInserted = false;
        if (apReadBehavior == null){
            isInserted = true;
            apReadBehavior = new ApReadBehavior();
            apReadBehavior.setId(sequences.sequenceApReadBehavior());
        }
        apReadBehavior.setEntryId(apBehaviorEntry.getId());
        apReadBehavior.setCount(dto.getCount());
        apReadBehavior.setPercentage(dto.getPercentage());
        apReadBehavior.setArticleId(dto.getArticleId());
        apReadBehavior.setLoadDuration(dto.getLoadDuration());
        apReadBehavior.setReadDuration(dto.getReadDuration());
        apReadBehavior.setCreatedTime(new Date());
        apReadBehavior.setUpdatedTime(new Date());
        apReadBehavior.setBurst("");

        int code = 0;
        if (isInserted){
            code = apReadBehaviorMapper.insert(apReadBehavior);
            if (code == 1){
                sender.sendMessagePlus(new UserReadMessage(apReadBehavior), userId, true);
            }
        }else{
            code = apReadBehaviorMapper.update(apReadBehavior);
        }
        return ResponseResult.okResult(code);
    }
}
