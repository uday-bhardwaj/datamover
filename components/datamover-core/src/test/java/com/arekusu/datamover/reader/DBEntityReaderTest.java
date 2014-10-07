package com.arekusu.datamover.reader;

import com.arekusu.datamover.dao.SimpleDBDAO;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class DBEntityReaderTest {

    DBEntityReader reader;

    SimpleDBDAO daoMock;

    File modelFile;

    @Before
    public void init() throws URISyntaxException {
        reader = new DBEntityReader();
        daoMock = mock(SimpleDBDAO.class);
        reader.setDao(daoMock);
        modelFile = new File(this.getClass().getResource("/models/simpleModel.xml").toURI());
    }

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
