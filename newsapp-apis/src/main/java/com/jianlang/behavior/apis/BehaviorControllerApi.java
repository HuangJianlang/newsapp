package com.jianlang.behavior.apis;

import com.jianlang.model.behavior.dtos.LikesBehaviorDto;
import com.jianlang.model.behavior.dtos.ReadBehaviorDto;
import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.behavior.dtos.UnLikesBehaviorDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface BehaviorControllerApi {
    /**
     * save user behavior about viewing
     * @param dto
     * @return
     */
    ResponseResult saveShowBehavior(ShowBehaviorDto dto);

    ResponseResult saveLikesBehavior(LikesBehaviorDto dto);

    ResponseResult saveUnlikesBehavior(UnLikesBehaviorDto dto);

    ResponseResult saveReadBehavior(ReadBehaviorDto dto);
}
