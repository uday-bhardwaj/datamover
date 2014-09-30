package com.arekusu.datamover.model;

import com.arekusu.datamover.model.jaxb.FieldType;

public class Field {
    private FieldType type;
    private String value;

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
