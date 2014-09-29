package com.arekusu.datamover.model;

import com.arekusu.datamover.exception.ModelReaderException;
import com.arekusu.datamover.model.jaxb.ModelType;
import com.arekusu.datamover.model.jaxb.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLFileModelReader implements ModelReader {
    private File model;

    @Override
    public ModelType readModel() {
        try {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ModelType) unmarshaller.unmarshal(model);
        } catch (JAXBException e) {
            throw new ModelReaderException("Error occurred while trying to parse model: " + e.getMessage(), e);
        }
    }

    public void setModel(File model) {
        this.model = model;
    }

    public File getModel() {
        return model;
    }


}
