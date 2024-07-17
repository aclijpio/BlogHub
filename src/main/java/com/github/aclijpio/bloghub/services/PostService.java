package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.services.dtos.comment.CommentRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostDto;
import com.github.aclijpio.bloghub.services.dtos.post.PostRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostUpdateRequest;

import java.util.List;

public interface PostService{

    List<PostDto> getAllPosts();
    List<PostDto> getPostsFromToday();
    List<PostDto> getPostsFromWeek();
    List<PostDto> getPostsFromMonth();
    PostDto getPostById(Long id);
    void createPost(PostRequest requestDto);
    void updatePost(Long postId, PostUpdateRequest requestDto);
    boolean deletePost(Long id);
    void postComment(Long postId, CommentRequest commentDto);

}
