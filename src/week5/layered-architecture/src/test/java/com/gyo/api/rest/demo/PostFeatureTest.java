package com.gyo.api.rest.demo;

import com.gyo.api.rest.demo.dtos.PostDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostFeatureTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("게시물 목록을 조회한다.")
    void list() {
        String url = "http://localhost:" + port + "/posts";
        String body = restTemplate.getForObject(url, String.class);

        assertThat(body).contains("content1");
        assertThat(body).contains("이건 없지");
    }

    @Test
    @DisplayName("게시물을 생성하고 목록을 조회한다.")
    void createAndGetList() {
        String url = "http://localhost:" + port + "/posts";
        PostDto postDto = new PostDto("2", "새제목", "제곧네");
        URI uri = restTemplate.postForLocation(url, postDto);
        String body = restTemplate.getForObject(url, String.class);
        // body에 포함 여부 조회
        assertThat(body).contains("제곧네");
        assertThat(body).contains("새제목");
        // id값 정확히 일치하는지 조회
        Pattern pattern = Pattern.compile("\"id\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(body);
        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group(1)).isEqualTo("2");
    }

    @Test
    @DisplayName("마지막 게시물을 삭제하고 목록을 조회해서 확인한다.")
    void delete() {
        String url = "http://localhost:" + port + "/posts";
        // 생성
        PostDto postDto = new PostDto("2", "새제목", "제곧네");
        URI uri = restTemplate.postForLocation(url, postDto);
        // 삭제
        String id = findLastId(restTemplate.getForObject(url, String.class));
        restTemplate.delete(url + "/" + id);
        // 목록 조회
        String body = restTemplate.getForObject(url, String.class);

        assertThat(body).doesNotContain("제곧네");
        assertThat(body).doesNotContain("2");
        assertThat(body).doesNotContain("새제목");
    }

    private String findLastId(String body) {
        Pattern pattern = Pattern.compile("\"id\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(body);

        String id = "";
        while (matcher.find()) {
            id = matcher.group(1);
        }
        return id;
    }

}
