package com.gyo.api.rest.demo.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultilineTextTest {

    @Test
    void creation() {
        MultilineText multilineText = new MultilineText("Hello\nWorld");

        assertEquals("Hello\nWorld", multilineText.toString());
    }
    
}