package com.github.aclijpio.bloghub.database.field.entity;

import lombok.Getter;

@Getter
public class EntityLine {

    private  final String fieldName;
    private final Class<?> fieldType;

    public EntityLine(String fieldName, Class<?> fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
