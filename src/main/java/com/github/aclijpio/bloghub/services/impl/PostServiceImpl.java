package com.github.aclijpio.bloghub.services.impl;

import com.github.aclijpio.bloghub.entities.Comment;
import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.exceptions.entity.PostNotFoundException;
import com.github.aclijpio.bloghub.exceptions.entity.UserNotFoundException;
import com.github.aclijpio.bloghub.repositories.impl.PostRepository;
import com.github.aclijpio.bloghub.repositories.impl.UserRepository;
import com.github.aclijpio.bloghub.services.PostService;
import com.github.aclijpio.bloghub.services.dtos.comment.CommentRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostDto;
import com.github.aclijpio.bloghub.services.dtos.post.PostRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostUpdateRequest;
import com.github.aclijpio.bloghub.services.mappers.PostMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PostServiceImpl implements PostService {

    PostRepository repository = PostRepository.INSTANCE;
    UserRepository userRepository = UserRepository.INSTANCE;
    PostMapper postMapper = PostMapper.INSTANCE;

    @Override
    public List<PostDto> getAllPosts() {
        return postMapper.toDtoList(repository.findAll());
    }

    @Override
    public List<PostDto> getPostsFromToday() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return postMapper.toDtoList(repository.findAllByDate(today));
    }
    @Override
    public List<PostDto> getPostsFromWeek() {

        LocalDateTime week = LocalDate.now().minusWeeks(1).atStartOfDay();

        return postMapper.toDtoList(repository.findAllByDate(week));
    }
    @Override
    public List<PostDto> getPostsFromMonth() {
        LocalDateTime month = LocalDate.now().minusMonths(1).atStartOfDay();
        return postMapper.toDtoList(repository.findAllByDate(month));
    }

    @Override
    public PostDto getPostById(Long id) {
        Optional<Post> optionalPost = repository.findById(id);

        Post post = optionalPost.orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));

        return postMapper.toDto(post);
    }

    @Override
    public void createPost(PostRequest requestDto) {

        Optional<User> optionalUser = userRepository.findById(requestDto.getUserId());
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found with id " + requestDto.getUserId()));


        Post post = new Post(
                user,
                requestDto.getTitle(),
                requestDto.getContent(),
                LocalDateTime.now()
        );



        repository.save(post);
    }

    @Override
    public void updatePost(Long id, PostUpdateRequest requestDto) {

        PostDto postDto = getPostById(id);

        postDto.setTitle(requestDto.getTitle());
        postDto.setContent(requestDto.getContent());
        postDto.setUpdatedDate(LocalDateTime.now());

        Post post = postMapper.toEntity(postDto);
        repository.save(post);
    }

    @Override
    public boolean deletePost(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public void postComment(Long postId, CommentRequest commentDto) {

        Comment comment = new Comment(commentDto.getContent());

        repository.savePostCommentLink(postId, comment);
    }
}
