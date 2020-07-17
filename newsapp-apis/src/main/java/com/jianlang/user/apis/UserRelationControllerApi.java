package com.jianlang.user.apis;

import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.dtos.UserRelationDto;

public interface UserRelationControllerApi {
    ResponseResult follow(UserRelationDto dto);
}
