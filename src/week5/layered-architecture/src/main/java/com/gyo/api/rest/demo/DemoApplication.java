package com.gyo.api.rest.demo;

import com.gyo.api.rest.demo.application.PostListService;
import com.gyo.api.rest.demo.application.PostService;
import com.gyo.api.rest.demo.controllers.PostController;
import com.gyo.api.rest.demo.repositories.PostRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(DemoApplication.class);

        PostController postController = context.getBean("postController", PostController.class);
        PostService postService = context.getBean("postService", PostService.class);
        PostListService postListService = context.getBean("postListService", PostListService.class);

        System.out.println("-".repeat(80));
        System.out.println("postController = " + postController);
        System.out.println("postListService = " + postListService);
        System.out.println("postService = " + postService);
        System.out.println("-".repeat(80));

//        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    PostService postService() {
        return new PostService(postRepository());
    }

    @Bean
    PostListService postListService() {
        return new PostListService(postRepository());
    }

    @Bean
    PostRepository postRepository() {
        System.out.println("*".repeat(80));
        System.out.println("PostRepository 생성");
        System.out.println("*".repeat(80));
        return new PostRepository();
    }

}
