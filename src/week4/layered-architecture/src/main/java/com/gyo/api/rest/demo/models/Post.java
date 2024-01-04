package com.gyo.api.rest.demo.models;

public class Post {
    private long id;
    private String title;
    private String content;

    public Post(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
