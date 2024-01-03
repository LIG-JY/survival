package com.gyo.api.rest.demo.application;

import com.github.f4b6a3.ulid.UlidCreator;
import com.gyo.api.rest.demo.dtos.PostDto;
import com.gyo.api.rest.demo.exceptions.PostNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PostService {

    private final List<PostDto> postDtoList = new ArrayList<>();

    public List<PostDto> list() {
        return this.postDtoList;
    }

    public PostDto detail(String id) {
        return findPost(id);
    }

    public PostDto create(PostDto body) {
        // Post 추가 기능
        String id = UlidCreator.getUlid().toString();
        body.setId(id);
        this.postDtoList.add(body);

        return body;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = findPost(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        PostDto foundPost = findPost(id);
        this.postDtoList.remove(foundPost);
    }

    private PostDto findPost(String id) {
        return postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }
}
