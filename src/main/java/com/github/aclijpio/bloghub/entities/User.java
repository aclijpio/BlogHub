package com.github.aclijpio.bloghub.entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long id;
    private String username;
    private String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
