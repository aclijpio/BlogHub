package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.repositories.PostRepository;

import java.util.List;

public class BlogService {


    private final PostRepository postRepository =  new PostRepository();

    public List<Post> getAllBlog(){
        return postRepository.findAll();
    }

    public void save(Post post) {
        postRepository.save(post);
    }



}
