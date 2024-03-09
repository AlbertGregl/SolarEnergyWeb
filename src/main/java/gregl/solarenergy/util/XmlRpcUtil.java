package gregl.solarenergy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlRpcUtil {

    private static final Logger logger = LoggerFactory.getLogger(XmlRpcUtil.class);

    private static Document parseXmlDocument(String xmlResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlResponse));
            return builder.parse(inputSource);
        } catch (Exception e) {
            logger.error("Error parsing XML response", e);
        }
        return null;
    }

    public static String parseTemperatureFromResponse(String xmlResponse) {
        Document doc = parseXmlDocument(xmlResponse);
        if (doc != null) {
            NodeList valueNodes = doc.getElementsByTagName("string");
            if (valueNodes.getLength() > 0) {
                return valueNodes.item(0).getTextContent();
            }
        }
        return null;
    }

    public static List<String> parseCityNamesFromResponse(String xmlResponse) {
        List<String> cityNames = new ArrayList<>();
        Document doc = parseXmlDocument(xmlResponse);
        if (doc != null) {
            NodeList cityNodes = doc.getElementsByTagName("string");
            for (int i = 0; i < cityNodes.getLength(); i++) {
                cityNames.add(cityNodes.item(i).getTextContent());
            }
        }
        return cityNames;
    }
}
