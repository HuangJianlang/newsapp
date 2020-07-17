package com.jianlang.login.service;

import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.pojos.ApUser;

public interface ApUserLoginService {
    //根据用户名与密码，登录验证
    ResponseResult loginAuth(ApUser user);
    //根据用户名与密码，登录验证
    ResponseResult loginAuthV2(ApUser user);
}
