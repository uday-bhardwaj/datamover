package com.arekusu.datamover.model;

import com.arekusu.datamover.exception.ModelReaderException;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLFileModelReader {
    public ModelType readModel(File model) {
        try {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ModelType) unmarshaller.unmarshal(model);
        } catch (JAXBException e) {
            throw new ModelReaderException("Error occurred while trying to parse model: " + e.getMessage(), e);
        }
    }
}
