package com.jianlang.model.mappers.app;

import com.jianlang.model.behavior.pojos.ApLikesBehavior;
import org.apache.ibatis.annotations.Param;

public interface ApLikesBehaviorMapper {
    ApLikesBehavior selectLastLike(@Param("burst") String burst, @Param("objectId")Integer objectId, @Param("entryId")Integer entryId,  @Param("type")Short type);

    int insert(ApLikesBehavior record);
}
