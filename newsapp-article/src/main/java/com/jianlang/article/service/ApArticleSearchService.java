package com.jianlang.article.service;

import com.jianlang.model.article.dtos.UserSearchDto;
import com.jianlang.model.common.dtos.ResponseResult;

public interface ApArticleSearchService {
    ResponseResult findUserSearch(UserSearchDto dto);

    ResponseResult delUserSearch(UserSearchDto dto);

    ResponseResult clearUserSearch(UserSearchDto dto);

    ResponseResult hotKeyWords(String date);

    ResponseResult searchAssociateWords(UserSearchDto dto);

    /**
     * 文章分页查询
     * @param dto
     * @return
     */
    ResponseResult esArticleSearch(UserSearchDto dto);

    ResponseResult saveUserSearch(Integer entryId, String searchWord);
}
