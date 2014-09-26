package com.arekusu.datamover;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.XMLFileModelReader;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.reader.EntityReader;
import com.arekusu.datamover.reader.TransportFileReader;
import com.arekusu.datamover.writer.EntityWriter;
import com.arekusu.datamover.writer.XMLFileEntityWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class DataMover {

    private static Logger logger = LoggerFactory.getLogger(DataMover.class);

    @Autowired
    private XMLFileModelReader modelReader;

    @Autowired
    private TransportFileReader transportFileReader;

    @Autowired
    private EntityWriter dbEntityWriter;

    @Autowired
    private EntityReader entityReader;

    @Autowired
    private XMLFileEntityWriter entityFileWriter;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/application-context.xml");
        DataMover dm = ctx.getBean(DataMover.class);
        dm.start(args);
    }

    public void start(String[] args) {
        if (args.length == 3) {
            if (args[0].equals("export")) {
                File modelFile = new File(args[1]);
                File outputFile = new File(args[2]);
                exportFile(outputFile, modelFile);
            } else if (args[0].equals("import")) {
                File modelFile = new File(args[1]);
                File inputFile = new File(args[2]);
                importFile(inputFile, modelFile);
            }
        } else {
            logger.error("Incorrect command!!");
            logger.error("Usage: export <modelFile> <outputFile>");
            logger.error("Usage: import <modelFile> <inputFile>");
        }

    }

    public void importFile(File transportFile, File model) {
        List<Entity> entities = transportFileReader.read(transportFile, modelReader.readModel(model));
        dbEntityWriter.write(entities);
    }

    public void exportFile(File outputFile, File model) {
        ModelType modelType = modelReader.readModel(model);
        List<Entity> entities = entityReader.readEntities(modelType.getDefinitionType().getEntityType());
        entityFileWriter.writeEntities(entities, modelType, outputFile);
    }
}
