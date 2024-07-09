package com.github.aclijpio.bloghub.database.util;

import com.github.aclijpio.bloghub.database.annotaions.Column;
import com.github.aclijpio.bloghub.database.annotaions.Entity;
import com.github.aclijpio.bloghub.database.annotaions.Id;
import com.github.aclijpio.bloghub.database.exceptions.IdNotSpecifiedException;
import com.github.aclijpio.bloghub.database.field.*;
import com.github.aclijpio.bloghub.database.exceptions.ColumnNameNotSpecifiedException;
import com.github.aclijpio.bloghub.database.annotaions.Relationship;


import java.lang.reflect.Field;

public class EntityAnnotationUtil {


    public static EntityInfo get(Class<?> clazz) {

        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity == null )
            throw new ColumnNameNotSpecifiedException("The table name is not specified.");

        Field [] fields = clazz.getDeclaredFields();

        EntityInfo entityInfo = new EntityInfo(entity.value());

        for (Field field : fields) {

            Column column = field.getAnnotation(Column.class);
            Relationship relationship = field.getAnnotation(Relationship.class);
            Id id = field.getAnnotation(Id.class);


            if (column != null)
                entityInfo.addField(
                        field.getName(),
                        new BasicEntityField(columNameConverter(column.value(), field))
                );
            else if (relationship != null)
                entityInfo.addField(
                        field.getName(),
                        new RelationshipEntityField(columNameConverter(relationship.mapperBy(), field), relationship.value())
                );
            else if (id != null) {
                if (entityInfo.isIdExists())
                    throw new IdNotSpecifiedException("There cannot be more than one a Id in a entity.");
                entityInfo.setId(columNameConverter(id.value(), field));
            }
            else
                throw new ColumnNameNotSpecifiedException("The column name is not specified.");
        }

        if (!entityInfo.isIdExists())
            throw new IdNotSpecifiedException("The id is not specified.");

        return entityInfo;
    }

    private static String columNameConverter(String columnName, Field field){
        if (columnName.isEmpty())
            return field.getName();
        return columnName;
    }


}
