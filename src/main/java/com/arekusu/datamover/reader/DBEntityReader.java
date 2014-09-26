package com.arekusu.datamover.reader;

import com.arekusu.datamover.dao.EntityDAO;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DBEntityReader implements EntityReader {

    @Autowired
    private EntityDAO dao;

    @Override
    public List<Entity> readEntities(EntityType entityType) {
        List<Entity> entities = dao.readSimpleEntity(entityType);

        if (entities != null) {
            for (Entity en : entities) {
                fillRefEntity(entityType, en);
            }
        }

        return entities;
    }

    private void fillRefEntity(EntityType entityType, Entity entity) {
        if (entityType.getReferencesType() != null) {
            for (EntityType en : entityType.getReferencesType().getEntityType()) {
                entity.getRefEntities().addAll(dao.readLinkedEntity(en, en.getDestinationField(), getFieldValue(entity, en.getSourceField())));
            }
        }
    }

    private String getFieldValue(Entity entity, String destinationField) {
        for (Field field : entity.getFields()) {
            if (field.getType().getAlias().equals(destinationField)) {
                return field.getValue();
            }
        }
        return null;
    }

}
