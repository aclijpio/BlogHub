package com.github.aclijpio.bloghub.utils;

import com.github.aclijpio.bloghub.configs.Column;
import com.github.aclijpio.bloghub.configs.Entity;
import com.github.aclijpio.bloghub.field.BasicEntityField;
import com.github.aclijpio.bloghub.field.EntityField;
import com.github.aclijpio.bloghub.configs.EntityInfo;
import com.github.aclijpio.bloghub.configs.exceptions.ColumnNameNotSpecifiedException;
import com.github.aclijpio.bloghub.field.Relationship;
import com.github.aclijpio.bloghub.field.RelationshipEntityField;


import java.lang.reflect.Field;

public class EntityAnnotationUtil {


    public static EntityInfo get(Class<?> clazz) {

        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity == null )
            throw new ColumnNameNotSpecifiedException("The table name is not specified.");
        Field [] fields = clazz.getDeclaredFields();

        EntityInfo entityInfo = new EntityInfo(entity.value());

        for (Field field : fields) {
            EntityField entityField = convert(field);
            entityInfo.addField(field.getName(), entityField);
        }
        return entityInfo;
    }


    private static EntityField convert(Field field) {
        Column column = field.getAnnotation(Column.class);
        Relationship relationship = field.getAnnotation(Relationship.class);

        EntityField entityField;

        if (column != null)
            entityField = new BasicEntityField(column);
        else if (relationship != null)
            entityField = new RelationshipEntityField(relationship);
        else
            throw new ColumnNameNotSpecifiedException("The column name is not specified.");

        return entityField;
    }

}
