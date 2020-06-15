package com.jianlang.model.mappers.app;

import com.jianlang.model.article.pojos.ApAssociateWords;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApAssociateWordsMapper {
    List<ApAssociateWords> selectByAssociateWords(@Param("searchWords") String searchWords, @Param("limit") int limit);
}
