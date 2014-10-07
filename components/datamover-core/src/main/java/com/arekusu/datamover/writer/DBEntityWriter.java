package com.arekusu.datamover.writer;

import com.arekusu.datamover.dao.EntityDAO;
import com.arekusu.datamover.exception.EntityWriterException;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.arekusu.datamover.model.jaxb.ModelType;

import java.util.List;

public class DBEntityWriter implements EntityWriter {

    private EntityDAO dao;

    @Override
    public void write(List<Entity> entities, ModelType modelType) {
        for (Entity en : entities) {
            writeEntity(en);
        }
    }

    private void writeEntity(Entity entity) {
        for (Entity en : entity.getLinkedEntities()) {
            writeEntity(en);
            copyField(en, entity, en.getType().getSourceField(), en.getType().getDestinationField());
        }

        dao.insertSimpleEntity(entity);

        for (Entity en : entity.getRefEntities()) {
            copyField(entity, en, en.getType().getSourceField(), en.getType().getDestinationField());
            writeEntity(en);
        }
    }

    private void copyField(Entity source, Entity dest, String sourceColumn, String destColumn) {
        if (sourceColumn == null) {
            throw new EntityWriterException("Source column attribute is required for Entity: " + source.getType().getAlias());
        }
        if (destColumn == null) {
            throw new EntityWriterException("Destination column attribute is required for Entity: " + source.getType().getAlias());
        }

        boolean fieldCopied = false;
        for (Field sourceField : source.getFields()) {
            if (sourceColumn.equals(sourceField.getType().getAlias())) {
                FieldType fieldType = new FieldType();
                fieldType.setColumn(destColumn);
                Field field = new Field();
                field.setType(fieldType);
                field.setValue(sourceField.getValue());
                dest.getFields().add(field);
                fieldCopied = true;
                break;
            }
        }
        if (!fieldCopied) {
            throw new EntityWriterException("Unable to find column with alias " + sourceColumn + " for entity " + source.getType().getAlias());
        }
    }

    public void setDao(EntityDAO dao) {
        this.dao = dao;
    }

}
