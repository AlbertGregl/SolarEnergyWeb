package gregl.solarenergy.service;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class RngValidationService {

    public boolean validateRng(String xmlData) {
        String rngSchema = readRngSchemaAsString();

        String validateUrl = "https://flask-production-b447.up.railway.app/api/validate";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject request = new JSONObject();
        request.put("xml", xmlData);
        request.put("rng", rngSchema);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(validateUrl, entity, String.class);

        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        JSONObject responseBody;
        try {
            responseBody = (JSONObject) parser.parse(response.getBody());
        } catch (ParseException e) {
            return false;
        }

        if (responseBody.containsKey("error")) {
            return false;
        }

        return (Boolean) responseBody.get("isValid");
    }

    private String readRngSchemaAsString() {
        try {
            ClassPathResource resource = new ClassPathResource("validation/energyData.rng");
            byte[] binaryData = Files.readAllBytes(Paths.get(resource.getURI()));
            return new String(binaryData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }
}
