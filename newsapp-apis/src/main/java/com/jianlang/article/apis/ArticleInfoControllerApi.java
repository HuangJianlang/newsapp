package com.jianlang.article.apis;

import com.jianlang.model.article.dtos.ArticleInfoDto;
import com.jianlang.model.common.dtos.ResponseResult;

//Controller 跟前端对接，因此传来的是dto
public interface ArticleInfoControllerApi {
    ResponseResult loadArticleInfo(ArticleInfoDto dto);
    ResponseResult loadArticleBehavior(ArticleInfoDto dto);
}
