package com.jianlang.model.mappers.app;

import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.article.pojos.ApArticleContent;

public interface ApArticleContentMapper {
    ApArticleContent selectByArticleId(Integer articleId);
}
