package com.github.aclijpio.bloghub.utils;

import com.github.aclijpio.bloghub.configs.EntityInfo;
import com.github.aclijpio.bloghub.entities.Blog;
import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.field.EntityField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

class EntityAnnotationUtilTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "almaz",
                "password",
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

        try(PreparedStatement preparedStatement = ConnectionPool.UTIL.getConnection().prepareStatement(query)){



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