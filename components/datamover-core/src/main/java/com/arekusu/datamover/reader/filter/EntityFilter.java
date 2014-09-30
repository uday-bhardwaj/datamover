package com.arekusu.datamover.reader.filter;

import com.arekusu.datamover.model.Entity;

public interface EntityFilter {
    public boolean isValidEntity(Entity entity);
}
