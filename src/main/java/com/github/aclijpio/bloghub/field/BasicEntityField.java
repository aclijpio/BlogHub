package com.github.aclijpio.bloghub.field;

import com.github.aclijpio.bloghub.configs.Column;
import com.github.aclijpio.bloghub.configs.exceptions.ColumnNameNotSpecifiedException;

public class BasicEntityField implements EntityField{

    private final String columnName;

    public BasicEntityField(Column column) {

        if (column == null)
            throw new ColumnNameNotSpecifiedException("The column name is not specified.");

        this.columnName = column.value();
    }

    @Override
    public String createQuery() {
        return this.columnName;
    }
}
