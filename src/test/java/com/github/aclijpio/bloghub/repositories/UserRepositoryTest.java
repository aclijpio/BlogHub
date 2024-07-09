package com.github.aclijpio.bloghub.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserRepositoryTest {


    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    public void findAllUsers(){
        userRepository.findAll();

    }

}