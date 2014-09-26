package com.arekusu.datamover.writer;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XMLFileEntityWriter {

    private static final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    public void writeEntities(List<Entity> entities, ModelType model, File outputFile) {
        IndentingXMLStreamWriter eventWriter = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
            eventWriter = new IndentingXMLStreamWriter(
                    outputFactory.createXMLStreamWriter(fos, "UTF-8"));
            eventWriter.writeStartDocument("UTF-8", "1.0");
            eventWriter.writeStartElement(model.getDefinitionType().getRootElement());
            for (Entity entity : entities) {
                writeEntity(entity, eventWriter);
            }
            eventWriter.writeEndElement();
            eventWriter.flush();
            eventWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (eventWriter != null) {
                    eventWriter.close();
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    private void writeEntity(Entity entity, IndentingXMLStreamWriter eventWriter) throws XMLStreamException {
        createStartNode(eventWriter, entity.getType().getAlias());
        for (Field field : entity.getFields()) {
            createNode(eventWriter, field.getType().getAlias(), field.getValue(), false);
        }
        for (Entity en : entity.getLinkedEntities()) {
            writeEntity(en, eventWriter);
        }
        for (Entity en : entity.getRefEntities()) {
            writeEntity(en, eventWriter);
        }
        createEndNode(eventWriter);

    }

    public static void createNode(IndentingXMLStreamWriter eventWriter,
                                  String name, String value, boolean isCdata) throws XMLStreamException {
        createStartNode(eventWriter, name);
        if (isCdata) {
            eventWriter.writeCData(value);
        } else {
            eventWriter.writeCharacters(value);
        }
        createEndNode(eventWriter);
    }

    public static void createStartNode(IndentingXMLStreamWriter eventWriter,
                                       String name) throws XMLStreamException {
        eventWriter.writeStartElement(name);
    }

    public static void createEndNode(IndentingXMLStreamWriter eventWriter)
            throws XMLStreamException {
        eventWriter.writeEndElement();
    }

}