package com.jianlang.model.mappers.app;

import com.jianlang.model.user.pojos.ApUserSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApUserSearchMapper {
    List<ApUserSearch> selectByEntryId(@Param("entryId")Integer entryId, @Param("limit")int limit);
    int delUserSearch(@Param("entryId")Integer entryId, @Param("hisIds")List<Integer> hisIds);
    int clearUserSearch(@Param("entryId")Integer entryId);
    int insert(ApUserSearch record);
    int checkExist(@Param("entryId") Integer entryId, @Param("keyword")String keyword);
}
