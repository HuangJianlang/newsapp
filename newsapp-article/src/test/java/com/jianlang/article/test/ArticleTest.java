package com.jianlang.article.test;

import com.jianlang.article.ArticleJarApplication;
import com.jianlang.article.service.AppArticleService;
import com.jianlang.common.article.constants.ArticleContants;
import com.jianlang.model.common.dtos.ResponseResult;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.apache.commons.net.nntp.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
public class ArticleTest {
    @Autowired
    private AppArticleService appArticleService;

    @Test
    public void testArticle(){
        ApUser user = new ApUser();
        user.setId(2028l);
        AppThreadLocalUtils.setUser(user);
        ResponseResult result = appArticleService.load(null, ArticleContants.LOADTYPE_LOAD_MORE);
        System.out.println(result.getData());
    }
}
