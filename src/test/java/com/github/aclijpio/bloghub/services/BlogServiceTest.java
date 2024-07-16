package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.entities.Comment;
import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.repositories.PostRepository;
import com.github.aclijpio.bloghub.repositories.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class BlogServiceTest {

    @Test
    void getAllBlog() {

        BlogService blogService = new BlogService();

        blogService.getAllBlog().forEach(System.out::println);


    }

    @Test
    void save() {
        BlogService blogService = new BlogService();

        UserRepository userRepository = new UserRepository();

        User commenter = new User("commenter", "mail@aclijpio.com");

        User author = new User("author", "gmail");

        User persistedAuthor = userRepository.save(author);

        Comment comment = new Comment("comment", commenter);

        Post post = new Post(null, "post", "content", LocalDateTime.now());



        blogService.save(post);


        PostRepository postRepository = new PostRepository();

    }
}