package com.github.aclijpio.bloghub.database.field;

import com.github.aclijpio.bloghub.database.annotaion.Relationship;

public class RelationshipEntityField extends EntityField {
    private final String columnName;
    private final RelationshipType type;

    public RelationshipEntityField(String mappedBy, RelationshipType type) {

        this.columnName = mappedBy;
        this.type = type;
    }


    @Override
    public String getColumnName() {
        return "";
    }
}
