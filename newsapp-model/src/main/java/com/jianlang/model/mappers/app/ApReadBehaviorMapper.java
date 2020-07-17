package com.jianlang.model.mappers.app;

import com.jianlang.model.behavior.pojos.ApReadBehavior;
import org.apache.ibatis.annotations.Param;

public interface ApReadBehaviorMapper {
    int insert(ApReadBehavior record);
    int update(ApReadBehavior record);
    ApReadBehavior selectByEntryId(@Param("burst")String burst, @Param("entryId")Integer entryId, @Param("articleId")Integer articleId);
}
