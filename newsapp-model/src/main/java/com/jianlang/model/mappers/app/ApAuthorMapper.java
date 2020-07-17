package com.jianlang.model.mappers.app;

import com.jianlang.model.article.pojos.ApAuthor;

public interface ApAuthorMapper {
    ApAuthor selectById(Integer id);
    ApAuthor selectByAuthorName(String authorName);
    void insert(ApAuthor apAuthor);
}
