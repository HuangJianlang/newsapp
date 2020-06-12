package com.jianlang.login.service.Impl;

import com.jianlang.model.behavior.dtos.FollowBehaviorDto;
import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import com.jianlang.model.behavior.pojos.ApFollowBehavior;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.ApFollowBehaviorMapper;
import com.jianlang.model.mappers.app.AppShowBehaviorEntryMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.login.service.AppFollowBehaviorService;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
public class AppFollowBehaviorServiceImpl implements AppFollowBehaviorService {

    @Autowired
    private AppShowBehaviorEntryMapper appShowBehaviorEntryMapper;
    @Autowired
    private ApFollowBehaviorMapper apFollowBehaviorMapper;

    @Override
    @Async
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto) {
        ApUser user = AppThreadLocalUtils.getUser();
        if(user == null && dto.getEquipmentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long id = null;
        if (user != null){
            id = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = appShowBehaviorEntryMapper.selectByUserIdOrEquipmentId(id, dto.getEquipmentId());
        if (apBehaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //save behavior
        ApFollowBehavior apFollowBehavior = new ApFollowBehavior();
        apFollowBehavior.setEntryId(apBehaviorEntry.getId());
        apFollowBehavior.setArticleId(dto.getArticleId());
        apFollowBehavior.setFollowId(dto.getFollowId());
        apFollowBehavior.setCreatedTime(new Date());

        int code = apFollowBehaviorMapper.insert(apFollowBehavior);

        return ResponseResult.okResult(code);
    }
}
