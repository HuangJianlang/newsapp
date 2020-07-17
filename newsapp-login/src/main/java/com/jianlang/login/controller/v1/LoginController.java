package com.jianlang.login.controller.v1;

import com.jianlang.login.apis.LoginControllerApi;
import com.jianlang.login.service.ApUserLoginService;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.pojos.ApUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    ApUserLoginService apUserLoginService;

    @Override
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody ApUser user) {
        return apUserLoginService.loginAuth(user);
    }
}
