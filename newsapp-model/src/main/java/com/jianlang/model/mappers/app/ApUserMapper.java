package com.jianlang.model.mappers.app;

import com.jianlang.model.user.pojos.ApUser;

public interface ApUserMapper {
    ApUser selectById(Integer id);
    ApUser selectByApPhone(String phone);
}
