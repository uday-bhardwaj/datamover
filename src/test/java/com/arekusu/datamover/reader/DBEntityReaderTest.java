package com.arekusu.datamover.reader;

import com.arekusu.datamover.model.Entity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/entityReaderTest.xml")
public class DBEntityReaderTest {

    @Autowired
    EntityReader reader;

    @Test
    public void readSimpleEntityTest(){
        List<Entity> entity = reader.readEntities(null);
    }
}
