package com.jianlang.article.controller.v1;

import com.jianlang.article.apis.ArticleHomeControllerApi;
import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.article.constants.ArticleContants;
import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController implements ArticleHomeControllerApi {

    @Autowired
    private AppArticleService appArticleService;

    @Override
    @GetMapping("/load")
    public ResponseResult load(ArticleHomeDto dto) {
        return appArticleService.load(dto, ArticleContants.LOADTYPE_LOAD_MORE);
    }

    @Override
    @GetMapping("/loadmore")
    public ResponseResult loadMore(ArticleHomeDto dto) {
        return appArticleService.load(dto, ArticleContants.LOADTYPE_LOAD_MORE);
    }

    @Override
    @GetMapping("/loadnew")
    public ResponseResult loadNew(ArticleHomeDto dto) {
        return appArticleService.load(dto, ArticleContants.LOADTYPE_LOAD_NEW);
    }
}
