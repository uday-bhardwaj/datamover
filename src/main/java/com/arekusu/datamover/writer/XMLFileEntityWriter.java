package com.arekusu.datamover.writer;

import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.sun.xml.txw2.output.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XMLFileEntityWriter {

    public static final String DOCUMENT_ROOT_NODE_NAME = "Data";

    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static final String DOCUMENT_ROOT_START_ELEMENT = "<"
            + DOCUMENT_ROOT_NODE_NAME + ">";

    public static final String DOCUMENT_ROOT_START_ELEMENT_NOT_CLOSED = "<"
            + DOCUMENT_ROOT_NODE_NAME;

    public static final String DOCUMENT_ROOT_END_ELEMENT = "</"
            + DOCUMENT_ROOT_NODE_NAME + ">";

    public static final String VERSION_ATTRIBUTE = "version";

    public static final String LINE_BREAK = System.getProperty(
            "line.separator", "\n");

    public static final String ENTITY_FILE_EXTENSION = ".xml";

    public static final String ENTITY_FILE_EXTENSION_UPCASE = ENTITY_FILE_EXTENSION
            .toUpperCase();


    private static final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

    public void writeEntities(List<Entity> entities) {
        String filePath = "out.xml";

        IndentingXMLStreamWriter eventWriter = null;
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            fos = new FileOutputStream(filePath);
            eventWriter = new IndentingXMLStreamWriter(
                    outputFactory.createXMLStreamWriter(fos, "UTF-8"));
            eventWriter.writeStartDocument("UTF-8", "1.0");
            eventWriter.writeStartElement(DOCUMENT_ROOT_NODE_NAME);
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
        for(Field field : entity.getFields()){
            createNode(eventWriter, field.getType().getAlias(), field.getValue().toString(), false);
        }
        for (Entity en : entity.getLinkedEntities()){
            writeEntity(en, eventWriter);
        }
        for (Entity en : entity.getRefEntities()){
            writeEntity(en, eventWriter);
        }
        createEndNode(eventWriter);

    }

    public static void createNode(IndentingXMLStreamWriter eventWriter,
                                  String name, String value, boolean isCdata) throws XMLStreamException {
        createStartNode(eventWriter, name);
        if (isCdata){
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