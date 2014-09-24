package com.arekusu.datamover.writer;

import com.arekusu.datamover.dao.SimpleDBDAO;
import com.arekusu.datamover.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;

public class DBEntityWriter implements EntityWriter {

    @Autowired
    SimpleDBDAO dao;

    @Override
    public void write(Entity entity) {
        for (Entity en: entity.getLinkedEntities()){
            write(en);
        }
        dao.insertSimpleEntity(entity);
        for (Entity en: entity.getRefEntities()){
            write(en);
        }
    }

}
