package com.arekusu.datamover.dao;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.test.util.EntityBuilder;
import com.arekusu.datamover.test.util.FieldBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimpleDBDAOTest {

    SimpleDBDAO dao;

    JdbcTemplate jdbcTemplate;

    @Before
    public void init(){
        dao = new SimpleDBDAO();
        jdbcTemplate = mock(JdbcTemplate.class);
        dao.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void insertSimpleEntityTest() {
        when(jdbcTemplate.update(any(String.class), (Object) any(String[].class))).thenReturn(1);

        Entity entity = new EntityBuilder()
                .withAlias("TestEntity")
                .withSchema("TestSchema")
                .withTable("TestTable")
                .withField(new FieldBuilder().withAlias("TestAlias1").withColumn("TestColumn1").withValue("TestValue1").build())
                .withField(new FieldBuilder().withAlias("TestAlias2").withColumn("TestColumn2").withValue("TestValue2").build())
                .build();

        dao.insertSimpleEntity(entity);
        verify(jdbcTemplate).update("INSERT INTO TestSchema.TestTable(TestColumn1,TestColumn2) VALUES (?,?);", (Object[]) new String[]{"TestValue1", "TestValue2"});
    }
}
