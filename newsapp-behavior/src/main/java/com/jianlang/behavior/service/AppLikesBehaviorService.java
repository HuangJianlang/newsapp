package com.jianlang.behavior.service;

import com.jianlang.model.behavior.dtos.LikesBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppLikesBehaviorService {
    ResponseResult saveLikesBehavior(LikesBehaviorDto dto);
}
