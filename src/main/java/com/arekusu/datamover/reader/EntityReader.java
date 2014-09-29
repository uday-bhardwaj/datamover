package com.arekusu.datamover.reader;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;

import java.util.List;

public interface EntityReader {
    List<Entity> readEntities(ModelType model);
}
