package com.github.aclijpio.bloghub.services.dtos.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostRequest {

    private Long userId;

    private String title;
    private String content;

}
