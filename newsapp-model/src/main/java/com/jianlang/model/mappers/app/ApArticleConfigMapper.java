package com.jianlang.model.mappers.app;

import com.jianlang.model.article.pojos.ApArticleConfig;

public interface ApArticleConfigMapper {
    ApArticleConfig selectByArticleId(Integer articleId);
}
