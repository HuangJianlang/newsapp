package com.jianlang.article.service;

import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface AppArticleService {
    /**
     * loadtype load more
     * @param dto
     * @param loadType
     * @return
     */
    ResponseResult load(ArticleHomeDto dto, Short loadType);
}
