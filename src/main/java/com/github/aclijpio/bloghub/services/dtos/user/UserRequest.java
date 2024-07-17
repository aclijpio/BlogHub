package com.github.aclijpio.bloghub.services.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

    private String username;
    private String email;

    public UserRequest() {
    }

    public UserRequest(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
