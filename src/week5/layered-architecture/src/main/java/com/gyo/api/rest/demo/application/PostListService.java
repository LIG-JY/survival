package com.gyo.api.rest.demo.application;

import com.gyo.api.rest.demo.Factory;
import com.gyo.api.rest.demo.dtos.PostDto;
import com.gyo.api.rest.demo.models.Post;
import com.gyo.api.rest.demo.repositories.PostRepository;

import java.util.List;

public class PostListService {

    private final PostRepository postRepository;

    public PostListService() {
        this.postRepository = Factory.postRepository();
    }

    public List<PostDto> list() {
        // domain repository에서 도메인을 가져온다.
        List<Post> posts = this.postRepository.findAll();

        // 가져온 도메인을 DTO로 변환한다.
        return posts.stream()
                .map(PostDto::new)
                .toList();
    }
}
