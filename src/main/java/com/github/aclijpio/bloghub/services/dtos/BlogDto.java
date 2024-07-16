package com.github.aclijpio.bloghub.services.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class BlogDto {

    private Long id;

    private UserDto user;

    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private List<CommentDto> comments;


}
