package com.arekusu.datamover.reader;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.EntityType;

import java.util.List;

public interface EntityReader {
    List<Entity> readEntities(EntityType entityType);
}
