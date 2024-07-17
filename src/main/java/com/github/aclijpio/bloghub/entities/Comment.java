package com.github.aclijpio.bloghub.entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {

    private Long id;
    private String content;

    public Comment() {
    }
    public Comment(String content) {
        this.content = content;
    }
}
