package com.arekusu.datamover.reader;

import com.arekusu.datamover.dao.EntityDAO;
import com.arekusu.datamover.model.Entity;
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
                if (entityType.getLinksType() != null) {
                    fillEntity(en.getLinkedEntities(), entityType.getLinksType().getEntityType());
                }
                if (entityType.getReferencesType() != null) {
                    fillEntity(en.getRefEntities(), entityType.getReferencesType().getEntityType());
                }
            }
        }

        return entities;
    }

    private void fillEntity(List<Entity> entities, List<EntityType> entityType) {
        for (EntityType en : entityType) {
            entities.addAll(readEntities(en));
        }
    }

}
