package com.gyo.api.rest.demo.repositories;

import com.gyo.api.rest.demo.exceptions.PostNotFoundException;
import com.gyo.api.rest.demo.models.MultilineText;
import com.gyo.api.rest.demo.models.Post;
import com.gyo.api.rest.demo.models.PostId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepository {

    private final Map<PostId, Post> posts;

    public PostRepository() {
        System.out.println("*".repeat(80));
        System.out.println("PostRepository 생성");
        System.out.println("*".repeat(80));
        this.posts = new HashMap<>();

        this.posts.put(PostId.of("1"),
                new Post(PostId.of("1"), "title1", MultilineText.of("content1")));
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    public Post find(PostId id) {
        Post post = posts.get(id);
        if (post == null) {
            throw new PostNotFoundException();
        }
        return post;
    }


    public void save(Post newPost) {
        this.posts.put(newPost.id(), newPost);
    }

    public void delete(Post post) {
        this.posts.remove(post.id());
    }
}
