package com.jianlang.article.controller.v1;

import com.jianlang.article.apis.ArticleSearchControllerApi;
import com.jianlang.article.service.ApArticleSearchService;
import com.jianlang.model.article.dtos.UserSearchDto;
import com.jianlang.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController implements ArticleSearchControllerApi {

    @Autowired
    private ApArticleSearchService apArticleSearchService;

    @Override
    @PostMapping("load_search_history")
    public ResponseResult findUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.findUserSearch(dto);
    }

    @Override
    @PostMapping("del_search")
    public ResponseResult delUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.delUserSearch(dto);
    }

    @Override
    @PostMapping("/clear_search")
    public ResponseResult clearUserSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.clearUserSearch(dto);
    }

    @Override
    @PostMapping("/load_hot_keywords")
    public ResponseResult hotKeyWords(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.hotKeyWords(dto.getHotDate());
    }

    @Override
    @PostMapping("/associate_search")
    public ResponseResult searchAssociateWords(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.searchAssociateWords(dto);
    }

    @Override
    @PostMapping("/article_search")
    public ResponseResult esArticleSearch(@RequestBody UserSearchDto dto) {
        return apArticleSearchService.esArticleSearch(dto);
    }
}
