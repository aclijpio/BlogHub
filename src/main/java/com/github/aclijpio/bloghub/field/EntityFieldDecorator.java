package com.github.aclijpio.bloghub.field;

public class EntityFieldDecorator implements EntityField{

    protected EntityField entityField;

    public EntityFieldDecorator(EntityField entityField) {
        this.entityField = entityField;
    }
    @Override
    public String createQuery() {
        return this.entityField.createQuery();
    }
}
