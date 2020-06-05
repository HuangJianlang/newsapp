package com.jianlang.article.apis;

import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface ArticleHomeControllerApi {
    /**
     * load article in Home page
     * @param dto
     * @return
     */
    ResponseResult load(ArticleHomeDto dto);

    ResponseResult loadMore(ArticleHomeDto dto);
    ResponseResult loadNew(ArticleHomeDto dto);
}
