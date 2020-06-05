package com.jianlang.model.mappers.app;

import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.user.pojos.ApUserArticleList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApArticleMapper {
    List<ApArticle> loadArticleListByIdList(@Param("list")List<ApUserArticleList> list);

    List<ApArticle> loadArticleListByLocation(@Param("dto") ArticleHomeDto dto, @Param("type") Short loadType);
}
