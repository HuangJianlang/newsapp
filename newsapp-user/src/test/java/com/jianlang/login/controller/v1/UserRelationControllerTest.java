package com.jianlang.login.controller.v1;

import com.jianlang.model.user.dtos.UserRelationDto;
import com.jianlang.model.user.pojos.ApUser;
import com.jianlang.login.UserJarApplication;
import com.jianlang.utils.threadlocal.AppThreadLocalUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserJarApplication.class)
@AutoConfigureMockMvc
public class UserRelationControllerTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void initUser(){
        ApUser user = new ApUser();
        user.setId(1l);
        AppThreadLocalUtils.setUser(user);
    }

    @Test
    public void testFollowAdd() throws Exception{
        UserRelationDto dto = new UserRelationDto();
        dto.setOperation((short)0);
        dto.setArticleId(1);
        dto.setAuthorId(1);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/user/user_follow");
        builder.contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
        Thread.sleep(10000);
    }

    @Test
    public void testFollowDelete() throws Exception{
        UserRelationDto dto = new UserRelationDto();
        dto.setOperation((short)1);
        dto.setArticleId(1);
        dto.setAuthorId(1);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/v1/user/user_follow");
        builder.contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(dto));
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
        Thread.sleep(10000);
    }
}
