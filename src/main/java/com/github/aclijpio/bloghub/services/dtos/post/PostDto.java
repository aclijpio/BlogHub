package com.github.aclijpio.bloghub.services.dtos.post;

import com.github.aclijpio.bloghub.services.dtos.comment.CommentDto;
import com.github.aclijpio.bloghub.services.dtos.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDto {

    private Long id;

    private UserDto user;

    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private List<CommentDto> comments;

    public PostDto() {
    }

    public PostDto(UserDto user, String title, String content, LocalDateTime createdDate) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = createdDate;
    }
}
