package com.jianlang.behavior.service;

import com.jianlang.model.behavior.dtos.UnLikesBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppUnlikesBehaviorService {

    ResponseResult saveUnlikeBehavior(UnLikesBehaviorDto dto);
}
