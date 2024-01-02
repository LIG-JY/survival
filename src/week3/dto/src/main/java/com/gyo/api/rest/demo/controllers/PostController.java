package com.gyo.api.rest.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyo.api.rest.demo.dtos.PostDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final ObjectMapper objectMapper;

    public PostController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Read
    @GetMapping("")
    public List<PostDto> list(HttpServletResponse response) {

        response.addHeader("Access-Control-Allow-Origin", "*");

        return List.of(
                new PostDto("1", "제목1", "내용1"),
                new PostDto("2", "제목2", "내용2"),
                new PostDto("3", "제목3", "내용3")
        );
    }

    @GetMapping("/{id}")
    public PostDto detail(
            @PathVariable("id") String id
    ) {
        // Return DTO
        return new PostDto(id, "제목", "Test Content");
    }

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody PostDto body) {
        return body.getId() + body.getContent() + body.getTitle();
    }
}