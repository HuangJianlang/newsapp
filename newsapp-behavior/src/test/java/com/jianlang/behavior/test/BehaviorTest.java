package com.jianlang.behavior.test;

import com.jianlang.behavior.BehaviorJarApplication;
import com.jianlang.behavior.service.AppLikesBehaviorService;
import com.jianlang.behavior.service.AppShowBehaviorService;
import com.jianlang.model.article.pojos.ApArticle;
import com.jianlang.model.behavior.dtos.LikesBehaviorDto;
import com.jianlang.model.behavior.dtos.ShowBehaviorDto;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = BehaviorJarApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BehaviorTest {
    @Autowired
    private AppShowBehaviorService appShowBehaviorService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testSave(){
        ApUser user = new ApUser();
        user.setId(2l);
        AppThreadLocalUtils.setUser(user);
        ShowBehaviorDto dto = new ShowBehaviorDto();
        List<ApArticle> articles = new ArrayList<>();
        ApArticle apArticle = new ApArticle();
        apArticle.setId(202);
        articles.add(apArticle);
        dto.setArticleIds(articles);
        appShowBehaviorService.saveShowBehavior(dto);
    }

    @Test
    public void testLike() throws Exception {
        LikesBehaviorDto dto = new LikesBehaviorDto();
        dto.setEntryId(1);
        dto.setEquipmentId(1);
        dto.setOperation((short) 0);
        dto.setType((short) 0);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/behavior/like_behavior")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Autowired
    private AppLikesBehaviorService appLikesBehaviorService;

    @Test
    public void testLikesSave(){
        ApUser user = new ApUser();
        user.setId(1l);
        AppThreadLocalUtils.setUser(user);
        LikesBehaviorDto dto = new LikesBehaviorDto();
        dto.setEntryId(1);
        dto.setOperation((short)0);
        dto.setType((short)0);
        appLikesBehaviorService.saveLikesBehavior(dto);
    }
}
