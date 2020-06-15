package com.jianlang.article.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jianlang.article.ArticleJarApplication;
import com.jianlang.model.article.dtos.UserSearchDto;
import com.jianlang.model.user.pojos.ApUserSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ArticleJarApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ArticleSearchControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testSearch() throws Exception {
        UserSearchDto dto = new UserSearchDto();
        dto.setEquipmentId(1);
        dto.setPageSize(5);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/search/load_search_history")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testDelSearch() throws Exception {
        UserSearchDto dto = new UserSearchDto();
        dto.setEquipmentId(1);
        ApUserSearch search = new ApUserSearch();
        search.setId(2);
        List<ApUserSearch> searches = new ArrayList<>();
        searches.add(search);
        dto.setHisList(searches);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/search/del_search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testClearSearch() throws Exception {
        UserSearchDto dto = new UserSearchDto();
        dto.setEquipmentId(1);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/search/clear_search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testHotSearch() throws Exception {
        UserSearchDto dto = new UserSearchDto();
        dto.setHotDate("2020-06-12");
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/search/load_hot_keywords")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAssociateSearch() throws Exception {
        UserSearchDto dto = new UserSearchDto();
        dto.setPageSize(5);
        dto.setSearchWords("CS");
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/search/associate_search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testEsSearch() throws Exception {
        UserSearchDto dto = new UserSearchDto();
        dto.setEquipmentId(1);
        dto.setPageSize(1);
        dto.setPageSize(10);
        dto.setSearchWords("CS");
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/article/search/article_search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
