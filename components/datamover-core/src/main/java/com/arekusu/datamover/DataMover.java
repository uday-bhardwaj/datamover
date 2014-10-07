package com.arekusu.datamover;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.ModelReader;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.reader.EntityReader;
import com.arekusu.datamover.writer.EntityWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataMover {

    private static Logger logger = LoggerFactory.getLogger(DataMover.class);

    private ModelReader modelReader;
    private EntityReader entityReader;
    private EntityWriter entityWriter;

    public void execute() {
        ModelType model = modelReader.readModel();
        List<Entity> entities = entityReader.readEntities(model);
        entityWriter.write(entities, model);
    }

    public ModelReader getModelReader() {
        return modelReader;
    }

    public void setModelReader(ModelReader modelReader) {
        this.modelReader = modelReader;
    }

    public EntityReader getEntityReader() {
        return entityReader;
    }

    public void setEntityReader(EntityReader entityReader) {
        this.entityReader = entityReader;
    }

    public EntityWriter getEntityWriter() {
        return entityWriter;
    }

    public void setEntityWriter(EntityWriter entityWriter) {
        this.entityWriter = entityWriter;
    }
}
