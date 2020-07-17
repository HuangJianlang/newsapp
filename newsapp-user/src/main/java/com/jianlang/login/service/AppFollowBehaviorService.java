package com.jianlang.login.service;

import com.jianlang.model.behavior.dtos.FollowBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppFollowBehaviorService {
    /**
     * 存储关注行为数据
     */
    ResponseResult saveFollowBehavior(FollowBehaviorDto dto);
}
