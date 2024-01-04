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

    public Post(String title, MultilineText content) {
        this.id = PostId.generate();
        this.title = title;
        this.content = content;
    }

    // getter
    public PostId id() {
        return id;
    }

    public String title() {
        return title;
    }

    public MultilineText content() {
        return content;
    }

    public void update(String title, MultilineText content) {
        this.title = title;
        this.content = content;
    }
}
