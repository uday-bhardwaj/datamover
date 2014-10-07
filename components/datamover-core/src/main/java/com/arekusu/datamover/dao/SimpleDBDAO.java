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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleDBDAO implements EntityDAO {
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(SimpleDBDAO.class);

    @Override
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


    @Override
    public List<Entity> readSimpleEntity(final EntityType entityType) {
        List<String> columns = fillColumns(entityType);

        String table = entityType.getFieldsType().getSchema() + "." + entityType.getFieldsType().getTable();
        String sqlString = "SELECT " + Joiner.on(",").join(columns) + " FROM " + table;

        List<Entity> entities = jdbcTemplate.query(sqlString + " ;", new EntityRowMapper(entityType));

        return entities;
    }

    @Override
    public List<Entity> readLinkedEntity(final EntityType entityType, String keyColumn, String keyValue) {
        List<String> columns = fillColumns(entityType);

        String table = entityType.getFieldsType().getSchema() + "." + entityType.getFieldsType().getTable();
        String sqlString = "SELECT " + Joiner.on(",").join(columns) + " FROM " + table;

        List<Entity> entities = null;

        if (keyValue != null) {
            sqlString += " WHERE " + keyColumn + " = ?";
            entities = jdbcTemplate.query(sqlString + " ;", new String[]{keyValue}, new EntityRowMapper(entityType));
        }

        return entities;
    }

    private List<String> fillColumns(EntityType entityType) {
        List<String> result = new ArrayList<String>();
        for (FieldType fieldType : entityType.getFieldsType().getFieldType()) {
            result.add(fieldType.getColumn());
        }
        if (entityType.getLinksType() != null) {
            for (EntityType enType : entityType.getLinksType().getEntityType()) {
                result.add(enType.getDestinationField());
            }
        }
        return result;
    }

    private String getFieldColumn(EntityType entity, String fieldAlias) {
        for (FieldType field : entity.getFieldsType().getFieldType()) {
            if (field.getAlias().equals(fieldAlias)) {
                return field.getColumn();
            }
        }
        return null;
    }

    private class EntityRowMapper implements RowMapper<Entity> {
        private final EntityType entityType;

        public EntityRowMapper(EntityType entityType) {
            this.entityType = entityType;
        }

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
            if (entityType.getLinksType() != null) {
                for (EntityType en : entityType.getLinksType().getEntityType()) {
                    entity.getLinkedEntities().addAll(readLinkedEntity(en, getFieldColumn(en, en.getSourceField()), rs.getString(en.getDestinationField())));
                }
            }
            return entity;
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
