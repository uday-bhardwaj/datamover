package com.arekusu.datamover.model;

import com.arekusu.datamover.model.jaxb.ModelType;

public interface ModelReader {
    ModelType readModel();
}
