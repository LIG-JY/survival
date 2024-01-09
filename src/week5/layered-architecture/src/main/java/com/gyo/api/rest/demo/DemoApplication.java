package com.gyo.api.rest.demo;

import com.gyo.api.rest.demo.application.PostListService;
import com.gyo.api.rest.demo.application.PostService;
import com.gyo.api.rest.demo.controllers.PostController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
}
