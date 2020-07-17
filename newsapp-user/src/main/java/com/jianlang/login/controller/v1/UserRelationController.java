package com.jianlang.login.controller.v1;

import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.dtos.UserRelationDto;
import com.jianlang.user.apis.UserRelationControllerApi;
import com.jianlang.login.service.AppUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserRelationController implements UserRelationControllerApi{

    @Autowired
    private AppUserRelationService appUserRelationService;

    @Override
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto) {
        return appUserRelationService.follow(dto);
    }
}
