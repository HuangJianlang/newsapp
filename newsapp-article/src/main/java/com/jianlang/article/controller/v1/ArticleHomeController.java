package com.jianlang.article.controller.v1;

import com.jianlang.article.apis.ArticleHomeControllerApi;
import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.article.constants.ArticleContants;
import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController implements ArticleHomeControllerApi {

    @Autowired
    private AppArticleService appArticleService;

    @Override
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return appArticleService.load(dto, ArticleContants.LOADTYPE_LOAD_MORE);
    }

    @Override
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return appArticleService.load(dto, ArticleContants.LOADTYPE_LOAD_MORE);
    }

    @Override
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return appArticleService.load(dto, ArticleContants.LOADTYPE_LOAD_NEW);
    }
}
