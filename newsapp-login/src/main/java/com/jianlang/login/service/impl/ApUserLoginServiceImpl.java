package com.jianlang.login.service.impl;

import com.jianlang.login.service.ApUserLoginService;
import com.jianlang.login.service.ValidateService;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.common.enums.AppHttpCodeEnum;
import com.jianlang.model.mappers.app.ApUserMapper;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.jwt.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class ApUserLoginServiceImpl implements ApUserLoginService {

    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private ValidateService validateService;

    @Override
    public ResponseResult loginAuth(ApUser user) {
        //验证参数
        if(StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser apUser = apUserMapper.selectByApPhone(user.getPhone());
        if (apUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }

        if (! user.getPassword().equals(apUser.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //不返回密码，保护措施
        apUser.setPassword("");
        Map<String, Object> data = new HashMap<>();
        data.put("token", AppJwtUtil.getToken(apUser));
        data.put("user", apUser);

        return ResponseResult.okResult(data);
    }

    @Override
    public ResponseResult loginAuthV2(ApUser user) {
        if(StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser dbUser = apUserMapper.selectByApPhone(user.getPhone());
        if (dbUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }

        boolean isValidate = validateService.validateMD5(user, dbUser);
        if (isValidate){
            user.setPassword("");
            Map<String, Object> data = new HashMap<>();
            data.put("token", AppJwtUtil.getToken(user));
            data.put("user", user);

            return ResponseResult.okResult(data);
        }

        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
    }
}
