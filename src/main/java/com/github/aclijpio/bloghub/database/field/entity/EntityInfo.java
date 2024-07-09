package com.github.aclijpio.bloghub.database.field.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
public class EntityInfo {

    private final String tableName;
    @Getter
    private final Map<String, EntityField> columnNames = new HashMap<>();

    @Setter
    private String id;

    public EntityInfo( String tableName) {
        this.tableName = tableName;
    }


    public void addField(String columnName, EntityField field) {
        columnNames.put(columnName, field);
    }

    public boolean isIdExists(){
        return id != null;
    }

    final public EntityPool getEntityPool(Object object){
        return EntityPool.create(this, object);
    }

}
