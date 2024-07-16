package com.github.aclijpio.bloghub.entities;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Post {
    private Long id;

    private User user;

    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private List<Comment> comments;

    public Post() {
    }
    public Post(User user, String title, String content, LocalDateTime createdDate) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = createdDate;
    }
}
