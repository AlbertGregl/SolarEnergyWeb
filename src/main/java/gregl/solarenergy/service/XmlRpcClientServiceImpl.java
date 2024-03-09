package gregl.solarenergy.service;

import gregl.solarenergy.util.XmlRpcUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class XmlRpcClientServiceImpl implements XmlRpcClientService {

    @Value("${xmlrpc.base.url}")
    private String xmlRpcBaseUrl;

    private String performXmlRpcCall(String methodName, Object... params) {
        StringBuilder paramXml = new StringBuilder();
        for (Object param : params) {
            paramXml.append("<param><value><string>")
                    .append(param.toString())
                    .append("</string></value></param>");
        }

        String requestXml = "<?xml version='1.0'?>" +
                "<methodCall>" +
                "<methodName>" + methodName + "</methodName>" +
                "<params>" + paramXml + "</params>" +
                "</methodCall>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> request = new HttpEntity<>(requestXml, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(xmlRpcBaseUrl + "/rpc", request, String.class);

        return response.getBody();
    }

    public String getTemperature(String city) {
        String xmlResponse = performXmlRpcCall("get_temperature", city);
        return XmlRpcUtil.parseTemperatureFromResponse(xmlResponse);
    }

    @Override
    public List<String> getCityNames() {
        String xmlResponse = performXmlRpcCall("get_city_names");
        return XmlRpcUtil.parseCityNamesFromResponse(xmlResponse);
    }

}