package com.jianlang.model.mappers.app;

import com.jianlang.model.article.pojos.ApHotWords;

import java.util.List;

public interface ApHotWordsMapper {
    List<ApHotWords> queryByHotDate(String hotDate);
}
