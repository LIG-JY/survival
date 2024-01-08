package com.gyo.api.rest.demo;

import com.gyo.api.rest.demo.repositories.PostRepository;

public class Factory {
    private static PostRepository postRepository;

    // 싱글톤 패턴
    public static PostRepository postRepository() {
        if (postRepository == null) {
            return new PostRepository();
        }
        return postRepository;
    }
}
