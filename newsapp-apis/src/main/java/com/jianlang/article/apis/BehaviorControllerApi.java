package com.jianlang.article.apis;

import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface BehaviorControllerApi {
    /**
     * save user behavior about viewing
     * @param dto
     * @return
     */
    ResponseResult saveShowBehavior(ShowBehaviorDto dto);
}
