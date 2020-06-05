package com.jianlang.model.mappers.app;

import com.jianlang.model.behavior.pojos.ApShowBehavior;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppShowBehaviorMapper {
    List<ApShowBehavior> selectListByEntryIdAndArticleIds(@Param("entryId") Integer id, @Param("articleIds") Integer[] articleIds);

    void saveShowBehavior(@Param("articleIds") Integer[] articleIds, @Param("entryId") Integer id);
}
