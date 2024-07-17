package com.github.aclijpio.bloghub.services.dtos.post;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostUpdateRequest {

    private String title;
    private String content;

}
