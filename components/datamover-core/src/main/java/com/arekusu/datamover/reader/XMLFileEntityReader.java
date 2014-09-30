package com.arekusu.datamover.reader;

import com.arekusu.datamover.exception.EntityReaderException;
import com.arekusu.datamover.model.Entity;
import com.arekusu.datamover.model.Field;
import com.arekusu.datamover.model.jaxb.EntityType;
import com.arekusu.datamover.model.jaxb.FieldType;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.reader.filter.EntityFilter;
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
import java.util.ArrayList;
import java.util.List;

public class XMLFileEntityReader implements EntityReader {

    Logger logger = LoggerFactory.getLogger(XMLFileEntityReader.class);

    private File transportFile;

    private EntityFilter filter;

    @Override
    public List<Entity> readEntities(ModelType model) {
        List<Entity> result = new ArrayList<Entity>();
        Document root = parseXmlTransportFile(transportFile);
        NodeList nodeList = root.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Entity en = fillEntity(nodeList.item(i), model.getDefinitionType().getEntityType());
            if (filter.isValidEntity(en)) {
                result.add(en);
            }
        }
        return result;
    }

    private Document parseXmlTransportFile(File transportFile) {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            return builder.parse(transportFile);
        } catch (ParserConfigurationException e) {
            throw new EntityReaderException("Error while configuring parser", e);
        } catch (SAXException e) {
            throw new EntityReaderException("Error while parsing file " + transportFile.getName(), e);
        } catch (IOException e) {
            throw new EntityReaderException("Error while reading file " + transportFile.getName(), e);
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

            throw new EntityReaderException("Unexpected tag: " + nodeItem);

        }

        return entity;
    }

    public File getTransportFile() {
        return transportFile;
    }

    public void setTransportFile(File transportFile) {
        this.transportFile = transportFile;
    }

    public EntityFilter getFilter() {
        return filter;
    }

    public void setFilter(EntityFilter filter) {
        this.filter = filter;
    }

}
