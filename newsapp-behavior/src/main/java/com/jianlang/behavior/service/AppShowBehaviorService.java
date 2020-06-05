package com.jianlang.behavior.service;

import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppShowBehaviorService {

    ResponseResult saveShowBehavior(ShowBehaviorDto dto);
}
