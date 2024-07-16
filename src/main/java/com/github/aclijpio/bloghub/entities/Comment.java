package com.github.aclijpio.bloghub.entities;



public class Comment {

    private Long id;
    private String content;
    private User author;

    public Comment(String content, User author) {
        this.content = content;
        this.author = author;
    }
}
