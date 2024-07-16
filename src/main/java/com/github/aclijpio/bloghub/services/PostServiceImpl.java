package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.exceptions.PostNotFoundException;
import com.github.aclijpio.bloghub.repositories.PostRepository;
import com.github.aclijpio.bloghub.services.dtos.PostDto;
import com.github.aclijpio.bloghub.services.mappers.PostMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PostServiceImpl implements PostService{


    PostRepository repository = new PostRepository();
    PostMapper mapper;

    @Override
    public void save(PostDto postDto) {

        Post post = mapper.toEntity(postDto);

        repository.save(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<PostDto> getPostsFromToday() {

        LocalDateTime today = LocalDate.now().atStartOfDay();


        return mapper.toDtoList(repository.findAllByDate(today));
    }
    @Override
    public List<PostDto> getPostsFromWeek() {

        LocalDateTime week = LocalDate.now().minusWeeks(1).atStartOfDay();

        return mapper.toDtoList(repository.findAllByDate(week));
    }
    @Override
    public List<PostDto> getPostsFromMonth() {
        LocalDateTime month = LocalDate.now().minusMonths(1).atStartOfDay();
        return mapper.toDtoList(repository.findAllByDate(month));
    }

    @Override
    public PostDto getPostById(Long id) {
        Optional<Post> optionalPost = repository.findById(id);

        Post post = optionalPost.orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));

        return mapper.toDto(post);
    }

    @Override
    public void createPost(PostDto postDto) {
        Post post = mapper.toEntity(postDto);

        repository.save(post);
    }

    @Override
    public void updatePost(PostDto postDto) {
        Post post = mapper.toEntity(postDto);

        repository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}
