package com.jianlang.model.mappers.app;

import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.model.user.pojos.ApUserArticleList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApUserArticleListMapper {
    List<ApUserArticleList> loadArticleIdListByUser(@Param("user") ApUser user, @Param("dto") ArticleHomeDto dto, @Param("type") Short loadType);
}
