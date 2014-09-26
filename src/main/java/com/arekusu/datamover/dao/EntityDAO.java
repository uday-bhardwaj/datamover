package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.EntityType;

import java.util.List;

public interface EntityDAO {
    void insertSimpleEntity(Entity entity);

    List<Entity> readSimpleEntity(EntityType entityType);

    List<Entity> readLinkedEntity(EntityType entityType, String keyColumn, String keyValue);
}
