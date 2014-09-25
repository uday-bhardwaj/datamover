package com.arekusu.datamover;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import com.arekusu.datamover.reader.EntityReader;
import com.arekusu.datamover.reader.TransportFileReader;
import com.arekusu.datamover.writer.EntityWriter;
import com.arekusu.datamover.writer.XMLFileEntityWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

@Component
public class DataMover {

    @Autowired
    private TransportFileReader fileReader;

    @Autowired
    private EntityWriter writer;

    @Autowired
    private EntityReader entityReader;

    @Autowired
    private XMLFileEntityWriter entityWriter;

    @Value("classpath:testTransportFile.xml")
    private File transportFile;

    @Value("classpath:testModel.xml")
    private File simpleModel;

    public static void main(String[] args) throws JAXBException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/application-context.xml");
        DataMover dm = ctx.getBean(DataMover.class);
        dm.start();
    }

    public void start() throws JAXBException {
        exportFile(new File("out.xml"), simpleModel);
        //importFile(transportFile, simpleModel);
    }

    public void importFile(File transportFile, File model) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType modelType = (ModelType) unmarshaller.unmarshal(model);

        Entity entity = fileReader.read(transportFile, modelType);
        writer.write(entity);
    }

    public void exportFile(File outputFile, File model) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType modelType = (ModelType) unmarshaller.unmarshal(model);

        List<Entity> entities = entityReader.readEntities(modelType.getDefinitionType().getEntityType());
        entityWriter.writeEntities(entities);
    }
}
