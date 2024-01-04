package com.gyo.api.rest.demo.models;

import java.util.Objects;

public class PostId {

    private String id;

    public PostId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostId postId)) return false;

        return Objects.equals(id, postId.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
