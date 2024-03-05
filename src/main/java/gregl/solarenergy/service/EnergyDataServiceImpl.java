package gregl.solarenergy.service;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class EnergyDataServiceImpl implements EnergyDataService{

    private final RestTemplate restTemplate;
    private final String energyDataApiBaseUrl;
    private final String tokenIssuer;
    private final String clientId;
    private final String clientSecret;

    public EnergyDataServiceImpl(RestTemplate restTemplate,
                             @Value("${energydata.api.baseurl}") String energyDataApiBaseUrl,
                             @Value("${token.issuer}") String tokenIssuer,
                             @Value("${okta.oauth2.client-id}") String clientId,
                             @Value("${okta.oauth2.client-secret}") String clientSecret) {
        this.restTemplate = restTemplate;
        this.energyDataApiBaseUrl = energyDataApiBaseUrl;
        this.tokenIssuer = tokenIssuer;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private String fetchToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("audience", energyDataApiBaseUrl);
        body.put("grant_type", "client_credentials");

        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenIssuer, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
            try {
                JSONObject responseBody = (JSONObject) parser.parse(response.getBody());
                return (String) responseBody.get("access_token");
            } catch (ParseException e) {
                throw new IllegalStateException("Failed to parse the token response: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalStateException("Failed to fetch token: " + response.getStatusCode());
        }
    }

    public String fetchData(String endpoint) {
        String accessToken = fetchToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(energyDataApiBaseUrl + endpoint, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error during data fetch: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

    public void addEnergyData(String xmlData) {
        String accessToken = fetchToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>(xmlData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(energyDataApiBaseUrl + "/api/save", HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error during data add: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }
}