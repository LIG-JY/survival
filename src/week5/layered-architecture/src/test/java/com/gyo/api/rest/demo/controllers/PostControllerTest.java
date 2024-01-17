package com.gyo.api.rest.demo.controllers;

import com.gyo.api.rest.demo.models.MultilineText;
import com.gyo.api.rest.demo.models.Post;
import com.gyo.api.rest.demo.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private PostRepository postRepository;

    @Test
    public void list() throws Exception {
        this.mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("title1")
                ));

        Mockito.verify(postRepository).findAll(); // findAll이 호출되었는지 확인
    }

    @Test
    public void create() throws Exception {
        String json = """
                {
                	"title": "새 글",
                	"content": "제곧내"
                }
                """;

        this.mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        Mockito.verify(postRepository).save(argThat(
                post -> {
                    return getFieldValue(post, "title").equals("새 글")
                            && getFieldValue(post, "content").equals(new MultilineText("제곧내"));
                }
        )); // save가 호출되었는지 확인
    }

    // Repository에 기본으로 Post(PostId.of("1"), "title1", MultilineText.of("content1"))) 존재
    @Test
    public void deletePost() throws Exception {
        String id = "1";
        this.mockMvc.perform(
                        delete("/posts/" + id))
                .andExpect(status().isOk());

        Mockito.verify(postRepository).delete(any(Post.class)); // delete가 호출되었는지 확인
    }

    private Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}