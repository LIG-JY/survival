package com.gyo.api.rest.demo.controllers;

import com.gyo.api.rest.demo.application.PostListService;
import com.gyo.api.rest.demo.application.PostService;
import com.gyo.api.rest.demo.dtos.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final PostService postService = new PostService();
    private final PostListService postListService = new PostListService();

    // Read
    @GetMapping("")
    public List<PostDto> list() {
        return postListService.list();
    }

    @GetMapping("/{id}")
    public PostDto detail(
            @PathVariable("id") String id
    ) {
        return this.postService.detail(id);
    }

    // Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto create(@RequestBody PostDto body) {
        return this.postService.create(body);
    }

    // Update
    @PatchMapping("/{id}")
    public PostDto update(@PathVariable("id") String id, @RequestBody PostDto body) {

        return this.postService.updatePost(id, body);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        this.postService.deletePost(id);
    }
}