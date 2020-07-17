package com.jianlang.article.service;

import com.jianlang.model.article.dtos.ArticleInfoDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppArticleInfoService {
    ResponseResult getArticleInfo(Integer articleId);

    ResponseResult loadArticleBehavior(ArticleInfoDto dto);
}
