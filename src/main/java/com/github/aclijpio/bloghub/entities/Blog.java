package com.github.aclijpio.bloghub.entities;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Blog {
    private Long id;
    private String name;
    private String content;
    private String category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private List<Comment> comments;


}
