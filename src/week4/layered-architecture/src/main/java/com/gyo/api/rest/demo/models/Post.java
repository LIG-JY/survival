package com.gyo.api.rest.demo.models;

public class Post {
    private PostId id;
    private String title;
    private MultilineText content;

    public Post(PostId id, String title, MultilineText content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
