package com.github.aclijpio.bloghub.entities;

import com.github.aclijpio.bloghub.database.annotaion.Column;
import com.github.aclijpio.bloghub.database.annotaion.Entity;
import com.github.aclijpio.bloghub.database.annotaion.Id;
import com.github.aclijpio.bloghub.database.annotaion.Relationship;
import com.github.aclijpio.bloghub.database.field.RelationshipType;
import lombok.Getter;
import lombok.Setter;

@Entity("user")
@Getter
@Setter
public class User {

    @Id
    private Long id;

    @Column("username")
    private String username;


    @Relationship(value = RelationshipType.ONE_TO_ONE, mapperBy = "blog_id")
    private Blog blog;

    public User(String username, Blog blog) {
        this.username = username;
        this.blog = blog;
    }
}
