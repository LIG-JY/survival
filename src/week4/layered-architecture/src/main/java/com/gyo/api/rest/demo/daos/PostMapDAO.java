package com.gyo.api.rest.demo.daos;

import com.gyo.api.rest.demo.dtos.PostDto;
import com.gyo.api.rest.demo.exceptions.PostNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostMapDAO {
    private final Map<String, PostDto> postDtoMap;

    public PostMapDAO() {
        this.postDtoMap = new HashMap<>();
        this.postDtoMap.put("1", new PostDto("1", "title1", "content1"));
    }

    public List<PostDto> findAll() {
        // ID 순으로 정렬되지 않는다. HashMap의 특징이다.
        return new ArrayList<>(this.postDtoMap.values());
    }

    public PostDto find(String id) {
        PostDto found = this.postDtoMap.get(id);
        // 못 찾을 경우 예외를 던진다.
        if (found == null) {
            throw new PostNotFoundException();
        }
        return found;
    }

    public void save(PostDto postDto) {
        this.postDtoMap.put(postDto.getId(), postDto);
    }

    public void delete(String id) {
        this.postDtoMap.remove(id);
    }
}
