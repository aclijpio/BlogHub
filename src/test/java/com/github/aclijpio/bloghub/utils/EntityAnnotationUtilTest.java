package com.github.aclijpio.bloghub.utils;

import com.github.aclijpio.bloghub.database.field.EntityInfo;
import com.github.aclijpio.bloghub.database.util.EntityAnnotationUtil;
import com.github.aclijpio.bloghub.entities.Blog;
import com.github.aclijpio.bloghub.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class EntityAnnotationUtilTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "almaz",
                new Blog()
        );
    }
    @Test
    public void findAllUser(){

        EntityInfo entityInfo = EntityAnnotationUtil.get(User.class);

        createQuery(entityInfo);

    }
    private void createQuery(EntityInfo entityInfo){


        String query = "select * from %s".formatted(entityInfo.getTableName());

        try(PreparedStatement preparedStatement = ConnectionPoolDefault.UTIL.getConnection().prepareStatement(query)){



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        System.out.println(entityInfo.getTableName());
        ;
        for (String field : entityInfo.getColumnNames().keySet()){
            System.out.println(field);
        }

    }
}