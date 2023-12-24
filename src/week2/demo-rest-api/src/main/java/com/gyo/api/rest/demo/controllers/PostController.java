package com.gyo.api.rest.demo.controllers;

import com.gyo.api.rest.demo.exceptions.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    // Read
    @GetMapping("/")
    public String list() {
        return "게시물 목록";
    }

    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") String id
    ) {
        if (id.equals("404")) {
            throw new PostNotFoundException();
        }
        return "게시물 상세: " + id;
    }

    // Create
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody String body) {
        return "{\"action\": \"게시물 생성\", \"body\": \""
                + body.replace("\"", "\\\"")
                + "\"}";
    }

    // Update
    @PatchMapping("/{id}")
    public String update(@PathVariable String id, @RequestBody String body) {
        return "{\"action\": \"게시물 수정: " + id + "\",\"body\": \""
                + body.replace("\"", "\\\"")
                + "\"}";
    }

    // Delete
    @DeleteMapping("/{id}")
    public String update(@PathVariable String id) {
        return "게시물 삭제 : " + id;
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String postNotFound() {
        return "게시물이 존재하지 않습니다.";
    }

}