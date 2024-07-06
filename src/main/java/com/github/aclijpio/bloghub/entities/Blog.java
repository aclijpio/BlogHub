package com.github.aclijpio.bloghub.entities;


import com.github.aclijpio.bloghub.database.annotaion.Column;
import com.github.aclijpio.bloghub.database.annotaion.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class Blog {
    @Id
    private Long id;
    @Column("name")
    private String name;
    @Column("content")
    private String content;
    @Column("category")
    private String category;
    @Column("createdDate")
    private LocalDateTime createdDate;

}
