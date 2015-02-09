package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.arekusu.datamover.model.jaxb.KeyValueElement;
import com.google.common.base.Splitter;
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
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        if (entity.getType().getKeyValueExtension() != null) {
            for (KeyValueElement el : entity.getType().getKeyValueExtension().getKeyValueElement()) {
                if (el.getKey().equals("StoreprocParamMapping")) {
                    res = mapNames(entity, el.getValue());
                }
            }
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entity.getType().getFieldsType().getSchema()).withProcedureName(entity.getType().getFieldsType().getTable()).declareParameters(
                res.keySet().toArray(new SqlParameter[res.size()])
        );

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        for(Map.Entry<SqlParameter, Object> entry : res.entrySet()){
            params.put(entry.getKey().getName(), entry.getValue());
        }

        SqlParameterSource in = new MapSqlParameterSource(params);
        Map<String, Object> simpleJdbcCallResult = call.execute(in);
    }

    private Map<SqlParameter, Object> mapNames(Entity entity, String value) {
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        for(String s : Splitter.on('|').split(value)){
            List<String> list = Splitter.on(':').limit(2).splitToList(s);
            res.put(new SqlParameter(list.get(0), Types.VARCHAR), tryToFill(entity, list.get(1)));
        }

        return res;
    }

    private Map<SqlParameter, Object> mapNamesByType(EntityType entity, String value, String keyValue) {
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        for(String s : Splitter.on('|').split(value)){
            List<String> list = Splitter.on(':').limit(2).splitToList(s);
            res.put(new SqlParameter(list.get(0), Types.VARCHAR), keyValue != null &&  list.get(1).equals("$Key")? keyValue : list.get(1));
        }

        return res;
    }

    private Object tryToFill(Entity entity, String s) {
        String res = s;
        if(s.startsWith("$")){
            String alias = s.replace("$","");
            for(Field field : entity.getFields()){
                if (field.getType().getAlias().equals(alias)){
                    return field.getValue();
                }
            }
        } else{
            return s;
        }

        return null;

    }


    @Override
    public List<Entity> readSimpleEntity(final EntityType entityType) {
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        if (entityType.getKeyValueExtension() != null){
            for(KeyValueElement el : entityType.getKeyValueExtension().getKeyValueElement()){
                if (el.getKey().equals("StoreprocParamMapping")){
                    res = mapNamesByType(entityType, el.getValue(), null);
                }
            }
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entityType.getFieldsType().getSchema()).withProcedureName(entityType.getFieldsType().getTable()).returningResultSet("rs", new EntityRowMapper(entityType)).declareParameters(
                res.keySet().toArray(new SqlParameter[res.size()]));

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        for(Map.Entry<SqlParameter, Object> entry : res.entrySet()){
            params.put(entry.getKey().getName(), entry.getValue());
        }

        SqlParameterSource in = new MapSqlParameterSource(params);

        Map<String, Object> result = call.execute(in);

        return (List<Entity>) result.get("rs");
    }

    @Override
    public List<Entity> readLinkedEntity(final EntityType entityType, String keyColumn, String keyValue) {
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        if (entityType.getKeyValueExtension() != null){
            for(KeyValueElement el : entityType.getKeyValueExtension().getKeyValueElement()){
                if (el.getKey().equals("StoreprocParamMapping")){
                    res = mapNamesByType(entityType, el.getValue(), keyValue);
                }
            }
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entityType.getFieldsType().getSchema()).withProcedureName(entityType.getFieldsType().getTable()).returningResultSet("rs", new EntityRowMapper(entityType)).declareParameters(
                res.keySet().toArray(new SqlParameter[res.size()]));

        List<Entity> entities = new ArrayList<Entity>();

        if (keyValue != null) {
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            for(Map.Entry<SqlParameter, Object> entry : res.entrySet()){
                params.put(entry.getKey().getName(), entry.getValue());
            }

            SqlParameterSource in = new MapSqlParameterSource(params);

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
