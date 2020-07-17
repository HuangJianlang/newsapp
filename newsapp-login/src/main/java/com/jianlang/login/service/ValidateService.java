package com.jianlang.login.service;

import com.jianlang.model.user.pojos.ApUser;

/**
 * DES MD5
 * MD5 + salt
 */
public interface ValidateService {
    boolean validateDES(ApUser user, ApUser userDb);
    boolean validateMD5WithSalt(ApUser user, ApUser userDb);
    boolean validateMD5(ApUser user, ApUser userDb);

}
