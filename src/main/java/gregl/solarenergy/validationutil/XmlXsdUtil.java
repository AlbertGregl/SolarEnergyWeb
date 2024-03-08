package gregl.solarenergy.validationutil;

import javax.xml.XMLConstants;

import gregl.solarenergy.model.GetEnergyDataByYearAndMonthResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlXsdUtil {

    private static final Logger logger = LoggerFactory.getLogger(XmlXsdUtil.class);

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

    public static GetEnergyDataByYearAndMonthResponse unmarshalEnergyData(String xmlData) throws JAXBException, SAXException, IOException {

        // extract the relevant XML part for unmarshalling
        String relevantXml = extractRelevantPart(xmlData);
        if (relevantXml == null) {
            throw new RuntimeException("Failed to extract relevant XML from SOAP response");
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(GetEnergyDataByYearAndMonthResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new ClassPathResource(LIST_SCHEMA_PATH).getFile());
        unmarshaller.setSchema(schema);

        // validate before returning
        try {
            validate(relevantXml, LIST_SCHEMA_PATH);
        } catch (SAXException | IOException e) {
            logger.error("Failed to validate the XML", e);
        }

        StreamSource xmlSource = new StreamSource(new StringReader(relevantXml));
        return (GetEnergyDataByYearAndMonthResponse) unmarshaller.unmarshal(xmlSource);
    }

    private static String extractRelevantPart(String soapXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(soapXml));
            Document doc = builder.parse(inputSource);

            NodeList nodes = doc.getElementsByTagName("ns2:getEnergyDataByYearAndMonthResponse");
            if (nodes.getLength() == 0) {
                return null;
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(nodes.item(0)), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            logger.error("Failed to extract relevant part of the XML", e);
            return null;
        }
    }

    private static void validate(String xmlData, String schemaPath) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new ClassPathResource(schemaPath).getFile());
        javax.xml.validation.Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xmlData)));
    }
}