package com.github.aclijpio.bloghub.configs;

import com.github.aclijpio.bloghub.field.EntityField;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@Getter
public class EntityInfo {

    private final String tableName;
    private final Map<String, EntityField> columnNames = new HashMap<>();

    public EntityInfo(String tableName) {
        this.tableName = tableName;
    }

    public void addField(String columnName, EntityField field) {
        columnNames.put(columnName, field);
    }

}
