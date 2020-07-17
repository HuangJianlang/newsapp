package com.jianlang.article.service;

import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.mess.app.ArticleVisitStreamDto;

public interface AppArticleService {
    /**
     * loadtype load more
     * @param dto
     * @param loadType
     * @return
     */
    ResponseResult load(ArticleHomeDto dto, Short loadType);

    ResponseResult updateArticleView(ArticleVisitStreamDto dto);

    ResponseResult loadV2(ArticleHomeDto dto,  Short loadType, boolean firstPage);
}
