package com.arekusu.datamover.reader;


import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import com.arekusu.datamover.reader.filter.EntityFilter;
import com.arekusu.datamover.test.util.EntityBuilder;
import com.arekusu.datamover.test.util.FieldBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XMLTransportFileReaderTest {

    private XMLFileEntityReader reader;

    private EntityFilter entityFilter;

    File modelFile;

    @Before
    public void init() throws URISyntaxException {
        reader = new XMLFileEntityReader();
        entityFilter = mock(EntityFilter.class);
        reader.setFilter(entityFilter);
        reader.setTransportFile(new File(this.getClass().getResource("/transportFiles/simpleTransportFile.xml").toURI()));
        modelFile = new File(this.getClass().getResource("/models/simpleModel.xml").toURI());
    }

    @Test
    @Ignore
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
