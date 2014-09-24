package com.arekusu.datamover.reader;

import com.arekusu.datamover.dao.SimpleDBDAO;
import com.arekusu.datamover.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;

public class DBEntityReader implements EntityReader {

    @Autowired
    private SimpleDBDAO dao;

    @Override
    public Entity readEntity() {
        return null;
    }
}
