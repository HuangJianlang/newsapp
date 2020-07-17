package com.jianlang.behavior.service;

import com.jianlang.model.behavior.dtos.ReadBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppReadBehaviorService {
    ResponseResult saveReadBehavior(ReadBehaviorDto dto);
}
