package com.arekusu.datamover;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.reader.TransportFileReader;
import com.arekusu.datamover.writer.EntityWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class DataMover {

    @Autowired
    private static TransportFileReader reader;

    @Autowired
    private static EntityWriter writer;

    public static void main(String[] args) {
        Entity entity = reader.read(null, null);
        writer.write(entity);
    }
}
