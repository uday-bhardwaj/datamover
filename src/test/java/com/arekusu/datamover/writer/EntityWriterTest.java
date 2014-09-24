package com.arekusu.datamover.writer;

import com.arekusu.datamover.dao.SimpleDBDAO;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/writerTest.xml")
public class EntityWriterTest {

    @Autowired
    EntityWriter writer;

    @Autowired
    SimpleDBDAO dao;

    @Test
    public void simpleWriteTest() throws JAXBException {
        FieldType fieldType = new FieldType();
        fieldType.setAlias("Test");
        Field field1 = new Field();
        field1.setType(fieldType);
        field1.setValue("Test");

        EntityType linkedEntType = new EntityType();
        linkedEntType.setAlias("LinkedEntity");
        linkedEntType.setSourceField("Src");
        linkedEntType.setDestinationField("Dest");
        FieldType linkedFieldType = new FieldType();
        linkedFieldType.setAlias("Src");
        Field linkedField = new Field();
        linkedField.setType(linkedFieldType);
        linkedField.setValue("LinkedFieldValue");
        Entity linkedEntity = new Entity();
        linkedEntity.setType(linkedEntType);
        linkedEntity.getFields().add(linkedField);

        EntityType refEntType = new EntityType();
        refEntType.setAlias("RefEntity");
        refEntType.setSourceField("Test");
        refEntType.setDestinationField("Dest");
        FieldType refFieldType = new FieldType();
        refFieldType.setAlias("Dest");
        Field refField = new Field();
        refField.setType(refFieldType);
        refField.setValue("RefFieldValue");
        Entity refEntity = new Entity();
        refEntity.setType(refEntType);
        refEntity.getFields().add(refField);

        EntityType entityType = new EntityType();
        entityType.setAlias("MainEntity");
        Entity en = new Entity();
        en.getFields().add(field1);
        en.getLinkedEntities().add(linkedEntity);
        en.getRefEntities().add(refEntity);

        Field refField2 = new Field();
        refField2.setValue("LinkedFieldValue");

        Entity reference = new Entity();
        reference.getFields().add(field1);
        reference.getFields().add(refField2);
        reference.getLinkedEntities().add(linkedEntity);
        reference.getRefEntities().add(refEntity);

        writer.write(en);

        ArgumentCaptor<Entity> entityCaptor = ArgumentCaptor.forClass(Entity.class);
        verify(dao, times(3)).insertSimpleEntity(entityCaptor.capture());
        verifyNoMoreInteractions(dao);
        assertThat(entityCaptor.getAllValues().get(0), sameBeanAs(linkedEntity));
        assertThat(entityCaptor.getAllValues().get(1), sameBeanAs(reference));
        assertThat(entityCaptor.getAllValues().get(2), sameBeanAs(refEntity));
    }
}
