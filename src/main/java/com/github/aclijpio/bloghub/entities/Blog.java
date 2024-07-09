package com.github.aclijpio.bloghub.entities;


import com.github.aclijpio.bloghub.database.annotaions.Column;
import com.github.aclijpio.bloghub.database.annotaions.Entity;
import com.github.aclijpio.bloghub.database.annotaions.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity("blogs")
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

    public Blog(String name, String content, String category, LocalDateTime createdDate) {
        this.name = name;
        this.content = content;
        this.category = category;
        this.createdDate = createdDate;
    }
}
