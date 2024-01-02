package com.gyo.api.rest.demo.controllers;

import com.gyo.api.rest.demo.dtos.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/posts")
public class PostController {

    private final List<PostDto> postDtoList = new ArrayList<>(List.of(
            new PostDto("1", "제목1", "내용1"),
            new PostDto("2", "제목2", "내용2"),
            new PostDto("3", "제목3", "내용3")
    ));

    // Read
    @GetMapping("")
    public List<PostDto> list() {
        return postDtoList;
    }

    // Create
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody PostDto body) {

        this.postDtoList.add(new PostDto("1004", body.getTitle(), body.getContent()));

        return body.getId() + body.getContent() + body.getTitle();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public PostDto delete(@PathVariable String id) {
        PostDto found = postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .get();

        this.postDtoList.remove(found);

        return found;
    }
}