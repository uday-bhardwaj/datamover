package com.arekusu.datamover.test.util;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldsType;
import com.arekusu.datamover.model.jaxb.LinksType;
import com.arekusu.datamover.model.jaxb.ReferencesType;

public class EntityBuilder {
    private final Entity entity;

    public EntityBuilder() {
        this.entity = new Entity();
        this.entity.setType(new EntityType());
        this.entity.getType().setFieldsType(new FieldsType());
    }

    public EntityBuilder(Entity entity) {
        this.entity = entity;
    }

    public EntityBuilder withAlias(String alias) {
        entity.getType().setAlias(alias);
        return this;
    }

    public EntityBuilder withDestinationField(String destination) {
        entity.getType().setDestinationField(destination);
        return this;
    }

    public EntityBuilder withSourceField(String source) {
        entity.getType().setSourceField(source);
        return this;
    }

    public EntityBuilder withSchema(String schema) {
        entity.getType().getFieldsType().setSchema(schema);
        return this;
    }

    public EntityBuilder withTable(String table) {
        entity.getType().getFieldsType().setTable(table);
        return this;
    }

    public EntityBuilder withField(Field field) {
        entity.getFields().add(field);
        entity.getType().getFieldsType().getFieldType().add(field.getType());
        return this;
    }

    public EntityBuilder withLinkedEntity(Entity linkedEntity) {
        entity.getLinkedEntities().add(linkedEntity);
        if (this.entity.getType().getLinksType() == null) {
            this.entity.getType().setLinksType(new LinksType());
        }
        entity.getType().getLinksType().getEntityType().add(linkedEntity.getType());
        return this;
    }


    public EntityBuilder withReferencedEntity(Entity refEntity) {
        entity.getRefEntities().add(refEntity);
        if (this.entity.getType().getReferencesType() == null) {
            this.entity.getType().setReferencesType(new ReferencesType());
        }
        entity.getType().getReferencesType().getEntityType().add(refEntity.getType());
        return this;
    }

    public Entity build() {
        return entity;
    }

}
