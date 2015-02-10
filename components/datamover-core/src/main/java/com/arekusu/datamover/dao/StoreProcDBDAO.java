package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.arekusu.datamover.model.jaxb.KeyValueElement;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
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
                if (el.getKey().equals("WriteStoreprocParamMapping")) {
                    res = mapNames(entity, entity.getType(), el.getValue(), entity.getType().getDestinationField());
                }
            }
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entity.getType().getFieldsType().getSchema()).withProcedureName(entity.getType().getFieldsType().getTable()).withoutProcedureColumnMetaDataAccess().declareParameters(
                res.keySet().toArray(new SqlParameter[res.size()])
        );

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        for(Map.Entry<SqlParameter, Object> entry : res.entrySet()){
            params.put(entry.getKey().getName(), entry.getValue());
        }

        SqlParameterSource in = new MapSqlParameterSource(params);
        Map<String, Object> simpleJdbcCallResult = call.execute(in);
    }

    private Map<SqlParameter, Object> mapNames(Entity entity, EntityType entityType, String value, String keyValue) {
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        for(String s : Splitter.on('|').split(value)){
            List<String> list = Splitter.on(':').limit(2).splitToList(s);
            res.put(new SqlParameter(list.get(0), Types.VARCHAR), tryToFill(entity, list.get(1), keyValue));
        }

        if (entityType.getKeyValueExtension() != null) {
            for (KeyValueElement el : entityType.getKeyValueExtension().getKeyValueElement()) {
                if (el.getKey().equals("OutStoreprocParamMapping")) {
                    res.putAll(mapOutParams(el.getValue()));
                }
            }
        }

        return res;
    }

    private Map<? extends SqlParameter, ?> mapOutParams(String value) {
        Map<SqlOutParameter, Object> res = new LinkedHashMap<SqlOutParameter, Object>();
        for (String s : Splitter.on("|").split(value)){
            List<String> list = Splitter.on(':').limit(2).splitToList(s);
            res.put(new SqlOutParameter(list.get(0), Integer.parseInt(list.get(1))), null);
        }
        return res;
    }

    private Object tryToFill(Entity entity, String s, String keyValue) {
        Map valuesMap = new HashMap();
        if(entity != null){
            for(Field field : entity.getFields()){
                valuesMap.put(field.getType().getAlias(), field.getValue());
            }
        }
        valuesMap.put("Key", keyValue);
        valuesMap.put("key", keyValue);
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        return sub.replace(s);
    }


    @Override
    public List<Entity> readSimpleEntity(final EntityType entityType) {
        Map<SqlParameter, Object> res = new LinkedHashMap<SqlParameter, Object>();
        if (entityType.getKeyValueExtension() != null){
            for(KeyValueElement el : entityType.getKeyValueExtension().getKeyValueElement()){
                if (el.getKey().equals("ReadStoreprocParamMapping")){
                    res = mapNames(null, entityType, el.getValue(), null);
                }
            }
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entityType.getFieldsType().getSchema()).withProcedureName(
                entityType.getFieldsType().getTable()).withoutProcedureColumnMetaDataAccess().
                returningResultSet("rs", new EntityRowMapper(entityType)).declareParameters(
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
                if (el.getKey().equals("ReadStoreprocParamMapping")){
                    res = mapNames(null, entityType, el.getValue(), keyValue);
                }
            }
        }

        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withSchemaName(entityType.getFieldsType().getSchema()).withProcedureName(entityType.getFieldsType().getTable()).withoutProcedureColumnMetaDataAccess().returningResultSet(
                "rs", new EntityRowMapper(entityType)).declareParameters(
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
                field.setValue(rs.getString(Integer.parseInt(fieldType.getOrder())));
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
