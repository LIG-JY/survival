package com.gyo.api.rest.demo.daos;

import com.gyo.api.rest.demo.dtos.PostDto;

import java.util.List;

public interface PostDAO {
    
    List<PostDto> findAll();

    PostDto find(String id);

    void save(PostDto postDto);

    void delete(String id);
}
