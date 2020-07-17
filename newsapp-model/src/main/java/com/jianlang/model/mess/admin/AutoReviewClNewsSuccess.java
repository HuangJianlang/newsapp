package com.jianlang.model.mess.admin;


import com.jianlang.model.article.pojos.ApArticleConfig;
import com.jianlang.model.article.pojos.ApArticleContent;
import com.jianlang.model.article.pojos.ApAuthor;
import lombok.Data;

@Data
public class AutoReviewClNewsSuccess {
    private ApArticleConfig apArticleConfig;
    private ApArticleContent apArticleContent;
    private ApAuthor apAuthor;

}
