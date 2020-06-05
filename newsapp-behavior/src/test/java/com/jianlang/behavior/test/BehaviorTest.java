package com.jianlang.behavior.test;

import com.jianlang.behavior.BehaviorJarApplication;
import com.jianlang.behavior.service.AppShowBehaviorService;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringRunner.class)
public class BehaviorTest {
    @Autowired
    private AppShowBehaviorService appShowBehaviorService;

    @Test
    public void testSave(){
        ApUser user = new ApUser();
        user.setId(1l);
        AppThreadLocalUtils.setUser(user);
        ShowBehaviorDto dto = new ShowBehaviorDto();
        List<ApArticle> articles = new ArrayList<>();
        ApArticle apArticle = new ApArticle();
        apArticle.setId(200);
        articles.add(apArticle);
        dto.setArticleIds(articles);
        appShowBehaviorService.saveShowBehavior(dto);
    }
}
