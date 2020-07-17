package com.jianlang.login.service;

import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.dtos.UserRelationDto;

public interface AppUserRelationService {
    ResponseResult follow(UserRelationDto dto);
}
