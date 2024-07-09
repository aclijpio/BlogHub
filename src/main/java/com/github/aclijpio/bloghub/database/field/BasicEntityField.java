package com.github.aclijpio.bloghub.database.field;

import com.github.aclijpio.bloghub.database.exceptions.ColumnNameNotSpecifiedException;
import com.github.aclijpio.bloghub.database.field.entity.EntityField;

public class BasicEntityField extends EntityField {

    private final String columnName;

    public BasicEntityField(String column) {
        if (column == null)
            throw new ColumnNameNotSpecifiedException("The column name is not specified.");
        this.columnName = column;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }
}
