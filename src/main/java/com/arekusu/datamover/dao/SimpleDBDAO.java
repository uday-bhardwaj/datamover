package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleDBDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(SimpleDBDAO.class);

    public void insertSimpleEntity(Entity entity) {
        Map<String, String> queryPairMap = new LinkedHashMap<String, String>();
        for (Field field : entity.getFields()) {
            queryPairMap.put(field.getType().getColumn(), field.getValue());
        }
        String table = entity.getType().getFieldsType().getSchema() + "." + entity.getType().getFieldsType().getTable();
        jdbcTemplate.update(
                "INSERT INTO " + table + "(" + Joiner.on(",").join(queryPairMap.keySet()) + ") VALUES (" + Joiner.on(",").join(Lists.transform(new ArrayList<String>(queryPairMap.values()), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return "?";
            }
        })) + ");", queryPairMap.values().toArray());
    }


}
