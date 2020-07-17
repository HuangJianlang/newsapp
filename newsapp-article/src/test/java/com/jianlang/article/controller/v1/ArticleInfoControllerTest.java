package com.jianlang.article.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jianlang.article.ArticleJarApplication;
import com.jianlang.model.article.dtos.ArticleInfoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ArticleInfoControllerTest {
    //使用mockMVC 测试
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void testLoadArticleInfo() throws Exception {
        ArticleInfoDto dto = new ArticleInfoDto();
        dto.setArticleId(1);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/load_article_info")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    public void testLoadArticleBehavior() throws Exception {
//        ArticleInfoDto dto = new ArticleInfoDto();
//        dto.setArticleId(1);
//        dto.setEquipmentId(1);
//        dto.setAuthorId(1);
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/load_article_behavior")
//                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsBytes(dto));
//        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
//    }
}
