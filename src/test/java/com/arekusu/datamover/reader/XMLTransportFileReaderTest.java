package com.arekusu.datamover.reader;


import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import com.arekusu.datamover.reader.filter.EntityFilter;
import com.arekusu.datamover.test.util.EntityBuilder;
import com.arekusu.datamover.test.util.FieldBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/readerTest.xml")
public class XMLTransportFileReaderTest {

    @Autowired
    private EntityReader reader;

    @Autowired
    private EntityFilter entityFilter;

    @Value("classpath:/models/simpleModel.xml")
    File modelFile;

    @Test
    public void readSimpleFileTest() throws JAXBException {
        when(entityFilter.isValidEntity(any(Entity.class))).thenReturn(true);

        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType model = (ModelType) unmarshaller.unmarshal(modelFile);

        List<Entity> rootEntity = reader.readEntities(model);

        Entity reference = new EntityBuilder()
                .withAlias("Element")
                .withSchema("TestSchema")
                .withTable("TestTable")
                .withField(new FieldBuilder().withAlias("Field1").withColumn("TestColumn").withValue("TestValue").build())
                .withField(new FieldBuilder().withAlias("Field2").withColumn("AnotherTestColumn").withValue("AnotherTestValue").build())
                .withLinkedEntity(new EntityBuilder()
                        .withAlias("LinkedElement")
                        .withSourceField("")
                        .withDestinationField("")
                        .withField(new FieldBuilder().withAlias("LinkedField").withColumn("LinkedColumn").withValue("LinkedValue").build())
                        .build())
                .withReferencedEntity(new EntityBuilder()
                        .withAlias("RefElement")
                        .withSourceField("")
                        .withDestinationField("")
                        .withField(new FieldBuilder().withAlias("RefField").withColumn("RefColumn").withValue("RefValue").build())
                        .build())
                .build();

        assertThat(rootEntity.get(0), sameBeanAs(reference));
    }
}
