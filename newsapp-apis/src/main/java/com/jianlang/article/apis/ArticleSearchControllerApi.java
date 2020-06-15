package com.jianlang.article.apis;

import com.jianlang.model.article.dtos.UserSearchDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface ArticleSearchControllerApi {
    ResponseResult findUserSearch(UserSearchDto dto);

    ResponseResult delUserSearch(UserSearchDto dto);

    ResponseResult clearUserSearch(UserSearchDto dto);

    ResponseResult hotKeyWords(UserSearchDto dto);

    ResponseResult searchAssociateWords(UserSearchDto dto);

    ResponseResult esArticleSearch(UserSearchDto dto);
}
