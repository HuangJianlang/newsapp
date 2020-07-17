package com.jianlang.login.service.impl;

import com.jianlang.login.service.ValidateService;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.common.DESUtils;
import com.jianlang.utils.common.MD5Utils;
import org.springframework.stereotype.Service;

@Service
public class ValidateServiceImpl implements ValidateService {
    @Override
    public boolean validateDES(ApUser user, ApUser userDb) {
        if (userDb.getPassword().equals(DESUtils.encode(user.getPassword()))){
            return true;
        }
        return false;
    }

    @Override
    public boolean validateMD5WithSalt(ApUser user, ApUser userDb) {
        if (userDb.getPassword().equals(MD5Utils.encodeWithSalt(user.getPassword(), userDb.getSalt()))){
            return true;
        }
        return false;
    }

    @Override
    public boolean validateMD5(ApUser user, ApUser userDb) {
        if (userDb.getPassword().equals(MD5Utils.encode(user.getPassword()))){
            return true;
        }
        return false;
    }
}
