package com.gyo.api.rest.demo.dtos;

public class PostDto {

    private String id;

    private String title;

    private String content;

    // Java Beans 규약을 따르는 클래스는 기본 생성자가 필요하다.
    public PostDto() {
    }

    public PostDto(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // DTO 특징 : 모든 필드에 대해서 Getter를 제공한다.
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
