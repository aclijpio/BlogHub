package com.github.aclijpio.bloghub.services.impl;

import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.exceptions.entity.UserNotFoundException;
import com.github.aclijpio.bloghub.repositories.impl.UserRepository;
import com.github.aclijpio.bloghub.services.UserService;
import com.github.aclijpio.bloghub.services.dtos.user.UserDto;
import com.github.aclijpio.bloghub.services.dtos.user.UserRequest;
import com.github.aclijpio.bloghub.services.mappers.UserMapper;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    UserRepository repository = UserRepository.INSTANCE;
    UserMapper mapper = UserMapper.INSTANCE;


    public List<UserDto> getAllUsers() {
        return mapper.toDtoList(repository.findAll());
    }
    public UserDto getUserById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User not found with id " + id)));
    }
    public void createUser(UserRequest userDto) {
        User user = new User(
                userDto.getUsername(),
                userDto.getEmail()
        );
        repository.save(user);
    }
    public void updateUser(Long id, UserRequest userDto) {

        Optional<User> optionalUser = repository.findById(id);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        repository.save(user);
    }
    public boolean deleteUser(Long id) {
        return repository.deleteById(id);
    }
}

