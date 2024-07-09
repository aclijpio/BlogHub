package com.github.aclijpio.bloghub;

import com.github.aclijpio.bloghub.database.annotaions.Column;
import com.github.aclijpio.bloghub.database.annotaions.Entity;
import com.github.aclijpio.bloghub.database.annotaions.Id;
import com.github.aclijpio.bloghub.database.field.entity.EntityInfo;
import com.github.aclijpio.bloghub.database.util.EntityAnnotationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntityAnnotationsTest {

    @Test
    public void testEntityAnnotations() {

        User user = new User();

        EntityInfo info = EntityAnnotationUtil.get(user.getClass());

        String tableName = info.getTableName();

        Assertions.assertEquals("users", tableName);
        Assertions.assertEquals("id", info.getId());



    }

    @Entity("users")
    public static class User{

        @Id
        private Long id;
        @Column
        private String name;

    }


}
