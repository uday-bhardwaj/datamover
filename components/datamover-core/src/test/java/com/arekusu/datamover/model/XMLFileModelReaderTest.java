package com.arekusu.datamover.model;

import com.arekusu.datamover.model.jaxb.DefinitionType;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import com.arekusu.datamover.test.util.EntityBuilder;
import com.arekusu.datamover.test.util.FieldBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class XMLFileModelReaderTest {

    private XMLFileModelReader modelReader;

    @Before
    public void init() throws URISyntaxException {
        modelReader = new XMLFileModelReader();
        modelReader.setModel(new File(this.getClass().getResource("/models/simpleModel.xml").toURI()));
    }

    @Test
    @Ignore
    public void simpleModelUnmarshallTest() {
        ModelType model = modelReader.readModel();

        EntityType referenceEntityType = new EntityBuilder()
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
                .build().getType();


        ModelType reference = new ObjectFactory().createModelType();
        DefinitionType def = new ObjectFactory().createDefinitionType();
        def.setEntityType(referenceEntityType);
        def.setRootElement("Transport");
        reference.setVersion("1.0");
        reference.setDefinitionType(def);
        assertThat(model, sameBeanAs(reference));
    }
}
