package com.gyo.api.rest.demo.models;

import com.github.f4b6a3.tsid.TsidCreator;

import java.util.Objects;

public class PostId {

    private String id;

    public PostId(String id) {
        this.id = id;
    }

    public static PostId of(String number) {
        return new PostId(number);
    }

    public static PostId generate() {
        return new PostId(TsidCreator.getTsid().toString());
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

    @Override
    public String toString() {
        return id;
    }
}
