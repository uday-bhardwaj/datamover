package com.arekusu.datamover.reader;

import com.arekusu.datamover.dao.SimpleDBDAO;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
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

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/entityReaderTest.xml")
public class DBEntityReaderTest {

    @Autowired
    EntityReader reader;

    @Autowired
    SimpleDBDAO daoMock;

    @Value("classpath:/models/simpleModel.xml")
    File modelFile;

    @Test
    public void readSimpleEntityTest() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType model = (ModelType) unmarshaller.unmarshal(modelFile);

        List<Entity> entities = reader.readEntities(model);

        verify(daoMock).readSimpleEntity(argThat(sameBeanAs(model.getDefinitionType().getEntityType())));
        verifyNoMoreInteractions(daoMock);
    }
}
