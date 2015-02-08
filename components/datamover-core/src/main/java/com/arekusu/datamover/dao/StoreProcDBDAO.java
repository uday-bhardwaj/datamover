package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class StoreProcDBDAO implements EntityDAO {
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(StoreProcDBDAO.class);

    @Override
    public void insertSimpleEntity(Entity entity) {
        Map<Integer, String> columnsMap = mapNames(entity);
        List<SqlParameter> res = new ArrayList<SqlParameter>();
        List<String> m = new ArrayList<String>(columnsMap.values());
        for (String column : m){
            res.add(new SqlParameter(column, Types.VARCHAR));
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entity.getType().getFieldsType().getSchema()).withProcedureName(entity.getType().getFieldsType().getTable()).declareParameters(
                res.toArray(new SqlParameter[res.size()])
        );

        Map<String, Object> params = new HashMap<String, Object>();
        for(String col : columnsMap.values()){
            params.put(col, null);
        }

        for (Field field : entity.getFields()) {
            params.put(field.getType().getColumn(), field.getValue());
        }

        SqlParameterSource in = new MapSqlParameterSource(params);
        Map<String, Object> simpleJdbcCallResult = call.execute(in);
    }

    private Map<Integer, String> mapNames(Entity entity) {
        Map<Integer, String> columns = new TreeMap<Integer, String>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        for (Field field : entity.getFields()){
            columns.put(Integer.parseInt(field.getType().getOrder() == null ? "0" : field.getType().getOrder()), field.getType().getColumn());
        }

        for (FieldType field : entity.getType().getFieldsType().getFieldType()){
            columns.put(Integer.parseInt(field.getOrder()), field.getColumn());
        }

        if (entity.getType().getLinksType() != null){
            for (EntityType entityType : entity.getType().getLinksType().getEntityType()){
                columns.put(Integer.parseInt(entityType.getDestinationFieldOrder()), entityType.getDestinationField());
            }
        }

        return columns;
    }


    @Override
    public List<Entity> readSimpleEntity(final EntityType entityType) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entityType.getFieldsType().getSchema()).withProcedureName(entityType.getFieldsType().getTable()).returningResultSet("rs", new EntityRowMapper(entityType));
        Map<String, Object> result = call.execute();

        return (List<Entity>) result.get("rs");
    }

    @Override
    public List<Entity> readLinkedEntity(final EntityType entityType, String keyColumn, String keyValue) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entityType.getFieldsType().getSchema()).withProcedureName(entityType.getFieldsType().getTable()).returningResultSet("rs", new EntityRowMapper(entityType));

        List<Entity> entities = new ArrayList<Entity>();

        if (keyValue != null) {
            call.declareParameters(new SqlParameter(keyColumn, Types.VARCHAR));

            Map<String, Object> inParamMap = new HashMap<String, Object>();
            inParamMap.put(keyColumn, keyValue);
            SqlParameterSource in = new MapSqlParameterSource(inParamMap);

            Map<String, Object> result = call.execute(in);
            entities = (List<Entity>) result.get("rs");
        }

        return entities;
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
