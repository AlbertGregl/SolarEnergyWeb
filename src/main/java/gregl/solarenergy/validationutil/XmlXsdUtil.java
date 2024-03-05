package gregl.solarenergy.validationutil;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlXsdUtil {

    private static final String SINGLE_SCHEMA_PATH = "validation/energyData.xsd";
    private static final String LIST_SCHEMA_PATH = "validation/energyDataList.xsd";

    public static String marshal(Object object) throws JAXBException, SAXException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        String xmlData = writer.toString();

        // validate before returning
        validate(xmlData, SINGLE_SCHEMA_PATH);
        return xmlData;
    }

    public static <T> T unmarshal(String xml, Class<T> clazz) throws JAXBException, SAXException, IOException {
        // validation before unmarshalling
        validate(xml, LIST_SCHEMA_PATH);

        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new ClassPathResource(LIST_SCHEMA_PATH).getFile());
        unmarshaller.setSchema(schema);

        StringReader reader = new StringReader(xml);
        return clazz.cast(unmarshaller.unmarshal(reader));
    }

    private static void validate(String xmlData, String schemaPath) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new ClassPathResource(schemaPath).getFile());
        javax.xml.validation.Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new java.io.StringReader(xmlData)));
    }
}