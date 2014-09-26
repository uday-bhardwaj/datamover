package com.arekusu.datamover.reader;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;

import java.io.File;
import java.util.List;

public interface TransportFileReader {
    List<Entity> read(File transportFile, ModelType model);
}
