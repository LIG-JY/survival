package com.gyo.api.rest.demo.application;

import com.github.f4b6a3.ulid.UlidCreator;
import com.gyo.api.rest.demo.daos.PostDAO;
import com.gyo.api.rest.demo.dtos.PostDto;

import java.util.List;

public class PostService {

    private final PostDAO postDAO;

    public PostService() {
        this.postDAO = new PostDAO();
    }

    public List<PostDto> list() {
        return this.postDAO.findAll();
    }

    public PostDto detail(String id) {
        return this.postDAO.find(id);
    }

    public PostDto create(PostDto body) {

        // 원본 body를 수정하지 않는다.
        String id = UlidCreator.getUlid().toString();
        PostDto newPostDto = new PostDto(id, body.getTitle(), body.getContent());

        // DAO에 저장한다.
        this.postDAO.save(newPostDto);

        return newPostDto;
    }

    public PostDto updatePost(String id, PostDto body) {
        PostDto foundPost = this.postDAO.find(id);

        foundPost.setId(id);
        foundPost.setTitle(body.getTitle());
        foundPost.setContent(body.getContent());

        return foundPost;
    }

    public void deletePost(String id) {
        this.postDAO.delete(id);
    }
}
