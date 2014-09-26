package com.arekusu.datamover.test.util;

import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.FieldType;

public class FieldBuilder {
    private final Field field;

    public FieldBuilder() {
        this.field = new Field();
        this.field.setType(new FieldType());
    }

    public FieldBuilder(Field field) {
        this.field = field;
    }

    public Field build() {
        return field;
    }

    public FieldBuilder withAlias(String alias) {
        field.getType().setAlias(alias);
        return this;
    }

    public FieldBuilder withColumn(String column) {
        field.getType().setColumn(column);
        return this;
    }

    public FieldBuilder withValue(String value) {
        field.setValue(value);
        return this;
    }
}
