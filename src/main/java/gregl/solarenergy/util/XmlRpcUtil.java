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

    public static List<String> parseCityNamesFromResponse(String xmlResponse) {
        List<String> cityNames = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlResponse));
            Document doc = builder.parse(inputSource);
            NodeList cityNodes = doc.getElementsByTagName("string");

            for (int i = 0; i < cityNodes.getLength(); i++) {
                cityNames.add(cityNodes.item(i).getTextContent());
            }
        } catch (Exception e) {
            logger.error("Error parsing city names from XML response", e);
        }
        return cityNames;
    }
}
