package com.jianlang.model.mappers.app;

import com.jianlang.model.behavior.pojos.ApBehaviorEntry;
import org.apache.ibatis.annotations.Param;

public interface AppShowBehaviorEntryMapper {
    ApBehaviorEntry selectByUserIdOrEquipmentId(@Param("userId") Long userId, @Param("equipmentId") Integer equipmentId);
}
