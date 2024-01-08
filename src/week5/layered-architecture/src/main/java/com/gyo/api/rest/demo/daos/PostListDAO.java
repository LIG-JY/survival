package com.gyo.api.rest.demo.daos;

import com.gyo.api.rest.demo.dtos.PostDto;
import com.gyo.api.rest.demo.exceptions.PostNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PostListDAO implements PostDAO {
    private final List<PostDto> postDtoList;

    public PostListDAO() {
        this.postDtoList = new ArrayList<>();
        this.postDtoList.add(new PostDto("1", "title1", "content1"));
    }

    @Override
    public List<PostDto> findAll() {
        // 읽기 전용 반환
        return new ArrayList<>(this.postDtoList);
    }

    @Override
    public PostDto find(String id) {
        return this.postDtoList.stream()
                .filter(postDto -> postDto.getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
    }

    @Override
    public void save(PostDto postDto) {
        this.postDtoList.add(postDto);
    }

    @Override
    public void delete(String id) {
        this.postDtoList.removeIf(postDto -> postDto.getId().equals(id));
    }
}
