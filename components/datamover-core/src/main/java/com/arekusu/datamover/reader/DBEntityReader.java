package com.arekusu.datamover.reader;

import com.arekusu.datamover.dao.EntityDAO;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.reader.filter.EntityFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBEntityReader implements EntityReader {

    private EntityDAO dao;

    private EntityFilter filter;

    @Override
    public List<Entity> readEntities(ModelType modelType) {
        EntityType entityType = modelType.getDefinitionType().getEntityType();
        List<Entity> unfilteredEntities = dao.readSimpleEntity(entityType);

        List<Entity> entities = new ArrayList<Entity>();
        if (filter == null){
            entities = unfilteredEntities;
        } else {
            for (Entity en : unfilteredEntities) {
                if (filter.isValidEntity(en)) {
                    entities.add(en);
                }
            }
        }

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
                entity.getRefEntities().addAll(filterEntities(dao.readLinkedEntity(en, en.getDestinationField(), getFieldValue(entity, en.getSourceField()))));
            }
        }
    }

    private Collection<Entity> filterEntities(List<Entity> entities) {
        if (filter == null){
            return entities;
        }
        List<Entity> res = new ArrayList<Entity>();
        for(Entity e : entities){
            if (filter.isValidEntity(e)){
                res.add(e);
            }
        }
        return res;
    }

    private String getFieldValue(Entity entity, String destinationField) {
        for (Field field : entity.getFields()) {
            if (field.getType().getAlias().equals(destinationField)) {
                return field.getValue();
            }
        }
        return null;
    }


    public EntityFilter getFilter() {
        return filter;
    }

    public void setFilter(EntityFilter filter) {
        this.filter = filter;
    }

    public void setDao(EntityDAO dao) {
        this.dao = dao;
    }
}
