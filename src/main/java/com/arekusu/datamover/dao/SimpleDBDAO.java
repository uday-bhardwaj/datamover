package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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


    public List<Entity> readSimpleEntity(final EntityType entityType) {
        List<String> columns = new ArrayList<String>();
        for (FieldType fieldType : entityType.getFieldsType().getFieldType()){
            columns.add(fieldType.getColumn());
        }
        String table = entityType.getFieldsType().getSchema() + "." + entityType.getFieldsType().getTable();
        List<Entity> entities = jdbcTemplate.query("SELECT " + Joiner.on(",").join(columns) + " FROM " + table + ";", new RowMapper<Entity>() {
            @Override
            public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
                Entity entity = new Entity();
                entity.setType(entityType);
                for (FieldType fieldType : entityType.getFieldsType().getFieldType()) {
                    Field field = new Field();
                    field.setType(fieldType);
                    field.setValue(rs.getString(fieldType.getColumn()));
                    entity.getFields().add(field);
                }
                return entity;
            }
        });

        return entities;
    }
}
