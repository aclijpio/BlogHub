package com.github.aclijpio.bloghub.entities;

import com.github.aclijpio.bloghub.database.annotaions.Column;
import com.github.aclijpio.bloghub.database.annotaions.Entity;
import com.github.aclijpio.bloghub.database.annotaions.Id;
import com.github.aclijpio.bloghub.database.annotaions.Relationship;
import com.github.aclijpio.bloghub.database.field.RelationshipType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity("Users")

public class User {

    @Id
    private Long id;

    @Column("username")
    private String username;


    @Relationship(value = RelationshipType.ONE_TO_ONE, mapperBy = "blog_id")
    private Blog blog;

    public User(String username) {
        this.username = username;
    }
}
