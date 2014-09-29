package com.arekusu.datamover.writer;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;

import java.util.List;

public interface EntityWriter {
    void write(List<Entity> entities, ModelType model);
}
