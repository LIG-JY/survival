package com.gyo.api.rest.demo.daos;

import com.gyo.api.rest.demo.dtos.PostDto;
import com.gyo.api.rest.demo.exceptions.PostNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    private final List<PostDto> postDtoList;

    public PostDAO() {
        this.postDtoList = new ArrayList<>();
        this.postDtoList.add(new PostDto("1", "title1", "content1"));
    }

    public List<PostDto> findAll() {
        return this.postDtoList;
    }

    public PostDto find(String id) {
        return this.postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }

    public void save(PostDto postDto) {
        this.postDtoList.add(postDto);
    }

    public void delete(String id) {
        this.postDtoList.removeIf(postDto -> postDto.getId().equals(id));
    }
}
