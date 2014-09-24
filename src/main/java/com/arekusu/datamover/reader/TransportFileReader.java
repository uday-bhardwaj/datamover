package com.arekusu.datamover.reader;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;

import java.io.File;

public interface TransportFileReader {
    Entity read(File transportFile, ModelType model);
}
