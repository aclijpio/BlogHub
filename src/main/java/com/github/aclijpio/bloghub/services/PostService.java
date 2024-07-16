package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.services.dtos.PostDto;

import java.util.List;

public interface PostService{

    void save(PostDto post);
    List<PostDto> getAllPosts();
    List<PostDto> getPostsFromToday();
    List<PostDto> getPostsFromWeek();
    List<PostDto> getPostsFromMonth();
    PostDto getPostById(Long id);
    void createPost(PostDto post);
    void updatePost(PostDto post);
    void deletePost(Long id);

}
