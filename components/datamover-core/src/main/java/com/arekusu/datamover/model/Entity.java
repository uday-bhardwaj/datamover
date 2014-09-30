package com.arekusu.datamover.model;

import com.arekusu.datamover.model.jaxb.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private EntityType type;
    private List<Field> fields = new ArrayList<Field>();
    private List<Entity> refEntities = new ArrayList<Entity>();
    private List<Entity> linkedEntities = new ArrayList<Entity>();

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Entity> getRefEntities() {
        return refEntities;
    }

    public List<Entity> getLinkedEntities() {
        return linkedEntities;
    }
}
