package com.arekusu.datamover.model.jaxb;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/testAppCtx.xml")
public class ModelUnmarshallTest {

    @Value("classpath:/models/simpleModel.xml")
    File simpleModel;

    @Test
    public void simpleModelUnmarshallTest() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ModelType model = (ModelType) unmarshaller.unmarshal(simpleModel);

        ObjectFactory objFactory = new ObjectFactory();

        FieldType field = objFactory.createFieldType();
        field.setAlias("TestAlias");
        field.setColumn("TestColumn");

        FieldsType fields = objFactory.createFieldsType();
        fields.getFieldType().add(field);

        EntityType entity = objFactory.createEntityType();
        entity.setFieldsType(fields);

        DefinitionType definition = objFactory.createDefinitionType();
        definition.setEntityType(entity);
        definition.setRootElement("TestRootElement");

        ModelType reference = objFactory.createModelType();
        reference.setVersion("1.0");

        reference.setDefinitionType(definition);

        assertThat(model, sameBeanAs(reference));
    }
}
