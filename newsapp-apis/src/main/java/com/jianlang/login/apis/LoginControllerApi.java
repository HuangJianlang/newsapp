package com.jianlang.login.apis;

import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.pojos.ApUser;

public interface LoginControllerApi {
    ResponseResult login(ApUser user);
}
