package com.arekusu.datamover.reader;


import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import org.junit.Ignore;
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

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/readerTest.xml")
public class XMLTransportFileReaderTest {

    @Autowired
    private TransportFileReader reader;

    @Value("classpath:/transportFiles/simpleTransportFile.xml")
    File simpleTransportFile;

    @Value("classpath:/models/simpleModel.xml")
    File modelFile;

    @Test
    public void readSimpleFileTest() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType model = (ModelType) unmarshaller.unmarshal(modelFile);

        Entity rootEntity = reader.read(simpleTransportFile, model);

        Field field1 = new Field();
        field1.setValue("TestValue");

        Field field2 = new Field();
        field2.setValue("AnotherTestValue");

        Entity reference = new Entity();
        reference.getFields().add(field1);
        reference.getFields().add(field2);

        assertThat(rootEntity, sameBeanAs(reference));
    }
}
