package com.arekusu.datamover.transformer;

import com.arekusu.datamover.model.Entity;

public interface EntityTransformer {
    Entity transform(Entity entity);
}
