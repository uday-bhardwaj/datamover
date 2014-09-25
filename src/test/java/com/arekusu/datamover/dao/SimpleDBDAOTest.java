package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.arekusu.datamover.model.jaxb.FieldsType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/simpleDBDAOTest.xml")
public class SimpleDBDAOTest {

    @Autowired
    EntityDAO dao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void insertSimpleEntityTest() {
        when(jdbcTemplate.update(any(String.class), any(String[].class))).thenReturn(1);
        EntityType entityType = new EntityType();
        FieldsType fieldsType = new FieldsType();
        fieldsType.setSchema("PUBLIC");
        fieldsType.setTable("TestTable1");
        entityType.setFieldsType(fieldsType);
        Entity entity = new Entity();
        entity.setType(entityType);
        FieldType fieldType1 = new FieldType();
        fieldType1.setAlias("TestAlias1");
        fieldType1.setColumn("TestColumn1");
        Field field1 = new Field();
        field1.setType(fieldType1);
        field1.setValue("TestValue1");

        FieldType fieldType2 = new FieldType();
        fieldType2.setAlias("TestAlias2");
        fieldType2.setColumn("TestColumn2");
        Field field2 = new Field();
        field2.setType(fieldType2);
        field2.setValue("TestValue2");

        entity.getFields().add(field1);
        entity.getFields().add(field2);
        dao.insertSimpleEntity(entity);
        verify(jdbcTemplate).update("INSERT INTO PUBLIC.TestTable1(TestColumn1,TestColumn2) VALUES (?,?);", new String[]{"TestValue1", "TestValue2"});
    }
}
