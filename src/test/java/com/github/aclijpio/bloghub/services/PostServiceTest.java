package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.exceptions.entity.PostNotFoundException;
import com.github.aclijpio.bloghub.exceptions.entity.UserNotFoundException;
import com.github.aclijpio.bloghub.repositories.impl.PostRepository;
import com.github.aclijpio.bloghub.repositories.impl.UserRepository;
import com.github.aclijpio.bloghub.services.dtos.post.PostDto;
import com.github.aclijpio.bloghub.services.dtos.post.PostRequest;
import com.github.aclijpio.bloghub.services.dtos.post.PostUpdateRequest;
import com.github.aclijpio.bloghub.services.impl.PostServiceImpl;
import com.github.aclijpio.bloghub.services.mappers.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;




    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPosts() {
        Post post = mock(Post.class);
        PostDto postDto = new PostDto();
        when(postRepository.findAll()).thenReturn(List.of(post));
        when(postMapper.toDtoList(anyList())).thenReturn(List.of(postDto));

        List<PostDto> result = postService.getAllPosts();


        assertNotNull(result);
        assertEquals(1, result.size());
        verify(postRepository, times(1)).findAll();
    }


    @Test
    void testGetPostsFromToday() {
        Supplier<Post> oldPost = () -> {
            Post post = mock(Post.class);
            post.setCreatedDate(LocalDateTime.now().minusWeeks(1));
            return post;
        };


        List<Post> posts = List.of(
                oldPost.get(),
                oldPost.get(),
                oldPost.get()
        );

        when(postRepository.findAllByDate(any(LocalDateTime.class))).thenReturn(posts);
        List<PostDto> result = postService.getPostsFromToday();

        result.forEach((r) -> System.out.println(r.getCreatedDate()));

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(postRepository, times(1)).findAllByDate(any(LocalDateTime.class));
        verify(postMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testGetPostById_NotFound() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    void testCreatePost_UserNotFound() {
        PostRequest request = new PostRequest();
        request.setUserId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> postService.createPost(request));
    }

    @Test
    void testUpdatePost() {
        Post post = new Post();
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("New Title");
        postDto.setContent("New Content");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postMapper.toDto(any(Post.class))).thenReturn(postDto);
        when(postMapper.toEntity(any(PostDto.class))).thenReturn(post);

        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle(updatedTitle);
        request.setContent(updatedContent);

        postService.updatePost(1L, request);


        PostDto updatedPost = postService.getPostById(1L);

        assertEquals(updatedTitle, updatedPost.getTitle());
        assertEquals(updatedContent, updatedPost.getContent());

        verify(postRepository, times(2)).findById(anyLong());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testDeletePost() {
        when(postRepository.deleteById(anyLong())).thenReturn(true);

        boolean result = postService.deletePost(1L);
        assertTrue(result);
        verify(postRepository, times(1)).deleteById(anyLong());
    }
}