package com.github.aclijpio.bloghub;

import com.github.aclijpio.bloghub.database.field.entity.EntityInfo;
import com.github.aclijpio.bloghub.database.field.entity.EntityPool;

import com.github.aclijpio.bloghub.database.util.EntityAnnotationUtil;
import com.github.aclijpio.bloghub.entities.Blog;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


public class EntityPoolTest {

    @Test
    public void getEntityPoolFromEntityInfo() {

        EntityInfo entityInfo = EntityAnnotationUtil.get(Blog.class);

        Blog blog = new Blog("First blog",
                            "Content",
                            "Test",
                                    LocalDateTime.now());

        EntityPool entityPool = entityInfo.getEntityPool(blog);


        System.out.println(entityPool.getLine());
    }


}
