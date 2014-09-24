package com.arekusu.datamover;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;
import com.arekusu.datamover.reader.TransportFileReader;
import com.arekusu.datamover.writer.EntityWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Component
public class DataMover {

    @Autowired
    private TransportFileReader reader;

    @Autowired
    private EntityWriter writer;

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
        importFile(transportFile, simpleModel);
    }

    public void importFile(File transportFile, File model) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType modelType = (ModelType) unmarshaller.unmarshal(model);

        Entity entity = reader.read(transportFile, modelType);
        writer.write(entity);
    }
}
