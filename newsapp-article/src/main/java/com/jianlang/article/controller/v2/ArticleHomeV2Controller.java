package com.jianlang.article.controller.v2;

import com.jianlang.article.apis.ArticleHomeControllerApi;
import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.article.constants.ArticleConstants;
import com.jianlang.model.article.dtos.ArticleHomeDto;
import com.jianlang.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/article")
public class ArticleHomeV2Controller implements ArticleHomeControllerApi {

    @Autowired
    private AppArticleService appArticleService;

    @Override
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {
        return appArticleService.loadV2(dto, ArticleConstants.LOADTYPE_LOAD_MORE, true);
    }

    @Override
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return appArticleService.loadV2(dto, ArticleConstants.LOADTYPE_LOAD_MORE, false);
    }

    @Override
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return appArticleService.loadV2(dto, ArticleConstants.LOADTYPE_LOAD_NEW, false);
    }
}
