package com.github.aclijpio.bloghub.entities;

import com.github.aclijpio.bloghub.configs.Column;
import com.github.aclijpio.bloghub.configs.Entity;
import com.github.aclijpio.bloghub.field.Relationship;
import com.github.aclijpio.bloghub.field.RelationshipType;
import lombok.Getter;
import lombok.Setter;

@Entity("user")
@Getter
@Setter
public class User {
    @Column("username")
    private String username;
    @Column("password")
    private String password;

    @Relationship(value = RelationshipType.ONE_TO_ONE, mapperBy = "blog_id")
    private Blog blog;

    public User(String username, String password, Blog blog) {
        this.username = username;
        this.password = password;
        this.blog = blog;
    }
}
