package com.gyo.api.rest.demo.models;

import java.util.List;

public class MultilineText {

    private final List<String> lines;

    public MultilineText(String text) {
        this.lines = List.of(text.split("\n"));
    }

    public static MultilineText of(String content) {
        return new MultilineText(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultilineText that)) return false;

        return lines.equals(that.lines);
    }

    @Override
    public int hashCode() {
        return lines.hashCode();
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
