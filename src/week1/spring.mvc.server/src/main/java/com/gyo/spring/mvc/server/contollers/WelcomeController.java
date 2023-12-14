package com.gyo.spring.mvc.server.contollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @GetMapping("/")
    String home() {
        return "Hello world!";
    }

    @GetMapping("/health")
    String health() {
        return "healthy!";
    }
}
