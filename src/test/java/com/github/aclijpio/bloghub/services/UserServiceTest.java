package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.exceptions.entity.UserNotFoundException;
import com.github.aclijpio.bloghub.repositories.impl.UserRepository;
import com.github.aclijpio.bloghub.services.dtos.user.UserDto;
import com.github.aclijpio.bloghub.services.dtos.user.UserRequest;
import com.github.aclijpio.bloghub.services.impl.UserServiceImpl;
import com.github.aclijpio.bloghub.services.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    UserRepository repository;
    @Mock
    UserMapper mapper;

    @InjectMocks
    UserServiceImpl userService;


    private User user;
    private UserDto userDto;
    private UserRequest userRequest;


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        user = new User("username", "email@example.com");
        userDto = new UserDto("username", "email@example.com");
        userRequest = new UserRequest("username", "email@example.com");
    }


    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        List<UserDto> userDtos = Arrays.asList(userDto);

        when(repository.findAll()).thenReturn(users);
        when(mapper.toDtoList(users)).thenReturn(userDtos);

        List<UserDto> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }

    @Test
    public void testGetUserById_UserExists() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    public void testGetUserById_UserDoesNotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testCreateUser() {
        when(mapper.toDto(any(User.class))).thenReturn(userDto);

        userService.createUser(userRequest);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserExists() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateUser(1L, userRequest);

        verify(repository, times(1)).save(user);
        assertEquals(userRequest.getUsername(), user.getUsername());
        assertEquals(userRequest.getEmail(), user.getEmail());
    }

    @Test
    public void testUpdateUser_UserDoesNotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userRequest));
    }

    @Test
    public void testDeleteUser() {
        when(repository.deleteById(anyLong())).thenReturn(true);

        boolean result = userService.deleteUser(1L);
        assertTrue(result);
    }



}