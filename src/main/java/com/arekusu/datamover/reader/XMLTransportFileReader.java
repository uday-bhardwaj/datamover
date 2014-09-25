package com.arekusu.datamover.reader;

import com.arekusu.datamover.exception.FileReaderException;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.arekusu.datamover.model.jaxb.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLTransportFileReader implements TransportFileReader {

    Logger logger = LoggerFactory.getLogger(XMLTransportFileReader.class);

    @Override
    public Entity read(File transportFile, ModelType model) {
        Document root = parseXmlTransportFile(transportFile);
        Entity result = null;
        result = fillEntity(root.getChildNodes().item(0).getChildNodes().item(1), model.getDefinitionType().getEntityType());

        return result;
    }

    private Document parseXmlTransportFile(File transportFile) {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            return builder.parse(transportFile);
        } catch (ParserConfigurationException e) {
            throw new FileReaderException("Error while configuring parser", e);
        } catch (SAXException e) {
            throw new FileReaderException("Error while parsing file " + transportFile.getName(), e);
        } catch (IOException e) {
            throw new FileReaderException("Error while reading file " + transportFile.getName(), e);
        }
    }

    private Entity fillEntity(Node node, EntityType entityType) {
        Entity entity = new Entity();
        entity.setType(entityType);

        NodeList nodeList = node.getChildNodes();
        mainCycle:
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Node nodeItem = nodeList.item(i);
            for (FieldType fieldType : entityType.getFieldsType().getFieldType()) {
                if (fieldType.getAlias().equals(nodeItem.getNodeName())) {
                    Field field = new Field();
                    field.setType(fieldType);
                    field.setValue(nodeItem.getTextContent().trim());
                    entity.getFields().add(field);
                    continue mainCycle;
                }
            }

            for (EntityType refEntityType : entityType.getReferencesType().getEntityType()) {
                if (refEntityType.getAlias().equals(nodeItem.getNodeName())) {
                    entity.getRefEntities().add(fillEntity(nodeItem, refEntityType));
                    continue mainCycle;
                }
            }

            for (EntityType linkEntityType : entityType.getLinksType().getEntityType()) {
                if (linkEntityType.getAlias().equals(nodeItem.getNodeName())) {
                    entity.getLinkedEntities().add(fillEntity(nodeItem, linkEntityType));
                    continue mainCycle;
                }
            }

            throw new FileReaderException("Unexpected tag: " + nodeItem);

        }

        return entity;
    }

}
