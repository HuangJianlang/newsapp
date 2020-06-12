package com.jianlang.behavior.service.impl;

import com.jianlang.behavior.service.AppUnlikesBehaviorService;
import com.jianlang.model.behavior.dtos.UnLikesBehaviorDto;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.behavior.pojos.ApUnlikesBehavior;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.ApUnlikesBehaviorMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppUnlikesBehaviorServiceImpl implements AppUnlikesBehaviorService {
    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;

    @Override
    public ResponseResult saveUnlikeBehavior(UnLikesBehaviorDto dto) {
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

        ApUnlikesBehavior alb = new ApUnlikesBehavior();
        alb.setEntryId(apBehaviorEntry.getId());
        alb.setCreatedTime(new Date());
        alb.setArticleId(dto.getArticleId());
        alb.setType(dto.getType());
        return ResponseResult.okResult(apUnlikesBehaviorMapper.insert(alb));
    }
}
