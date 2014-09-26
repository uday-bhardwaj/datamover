package com.arekusu.datamover.writer;

import com.arekusu.datamover.model.Entity;

import java.util.List;

public interface EntityWriter {
    public void write(List<Entity> entities);
}
