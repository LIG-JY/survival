package com.gyo.api.rest.demo.application;

import com.gyo.api.rest.demo.daos.PostDAO;
import com.gyo.api.rest.demo.daos.PostMapDAO;
import com.gyo.api.rest.demo.dtos.PostDto;
import com.gyo.api.rest.demo.models.MultilineText;
import com.gyo.api.rest.demo.models.Post;
import com.gyo.api.rest.demo.models.PostId;
import com.gyo.api.rest.demo.repositories.PostRepository;

import java.util.List;

public class PostService {

    private final PostDAO postDAO;

    private final PostRepository postRepository;

    public PostService() {
        // 인터페이스만 알면 좋은데 구현도 알게되서 아쉽다.
        this.postDAO = new PostMapDAO();
        this.postRepository = new PostRepository();
    }

    public List<PostDto> list() {
        // domain repository에서 도메인을 가져온다.
        List<Post> posts = this.postRepository.findAll();

        // 가져온 도메인을 DTO로 변환한다.
        return posts.stream()
                .map(PostDto::new)
                .toList();
    }

    public PostDto detail(String id) {

        // 도메인 찾기
        Post found = this.postRepository.find(PostId.of(id));

        // 도메인을 DTO로 변환한다.
        return new PostDto(found);
    }

    public PostDto create(PostDto body) {

        // 도메인 생성
        Post newPost = new Post(
                body.getTitle(),
                MultilineText.of(body.getContent())
        );

        // 도메인 저장
        this.postRepository.save(newPost);

        // 도메인을 DTO로 변환한다.
        return new PostDto(newPost);
    }

    public PostDto updatePost(String id, PostDto body) {

        // 도메인을 찾는다.
        Post foundPost = this.postRepository.find(PostId.of(id));

        // 도메인을 수정한다.
        foundPost.update(
                body.getTitle(),
                MultilineText.of(body.getContent())
        );

        return new PostDto(foundPost);
    }

    public void deletePost(String id) {

        // 도메인을 찾는다.
        Post foundPost = this.postRepository.find(PostId.of(id));

        // 도메인을 삭제한다.
        this.postRepository.delete(foundPost);
    }
}
