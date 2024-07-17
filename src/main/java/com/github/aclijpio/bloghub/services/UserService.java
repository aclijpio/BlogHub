package com.github.aclijpio.bloghub.services;

import com.github.aclijpio.bloghub.services.dtos.user.UserDto;
import com.github.aclijpio.bloghub.services.dtos.user.UserRequest;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    void createUser(UserRequest userDto);
    void updateUser(Long id, UserRequest userDto);
    boolean deleteUser(Long id);


}
