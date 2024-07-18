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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    private PostMapper postMapper = PostMapper.INSTANCE;

    @InjectMocks
    private PostServiceImpl postService;

    Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        post = mock(Post.class);
    }




    @Test
    void testGetAllPosts() {
        Post post = mock(Post.class);
        PostDto postDto = new PostDto();
        when(postRepository.findAll()).thenReturn(List.of(post));

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
        assertEquals(3, result.size());

        verify(postRepository, times(1)).findAllByDate(any(LocalDateTime.class));
        verify(postMapper, times(1)).toDtoList(anyList());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
    void testGetPostById(Long id){
        Post curPost = mock(Post.class);
        when(curPost.getId()).thenReturn(id);

        when(postRepository.findById(id)).thenReturn(Optional.of(curPost));

        PostDto result = postService.getPostById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(postRepository, times(1)).findById(id);


    }

    @ParameterizedTest
    @ValueSource(longs = {10, 23, 45, 56, 76, 87, 97, 111})
    void testGetPostById_NotFound(Long id) {
        when(postRepository.findById(id)).thenReturn(Optional.empty());

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

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        PostUpdateRequest request = new PostUpdateRequest();

        postService.updatePost(1L, request);

        verify(postRepository, times(1)).findById(anyLong());
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