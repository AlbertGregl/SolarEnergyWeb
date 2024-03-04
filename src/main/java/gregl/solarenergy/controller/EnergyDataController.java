package gregl.solarenergy.controller;

import gregl.solarenergy.service.EnergyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnergyDataController {

    private final EnergyDataService energyDataService;

    @Autowired
    public EnergyDataController(EnergyDataService energyDataService) {
        this.energyDataService = energyDataService;
    }

    @GetMapping("/api/data/{year}/{month}")
    public ResponseEntity<String> fetchData(@PathVariable String year, @PathVariable String month) {
        String endpoint = "/api/data/" + year + "/" + month;
        String data = energyDataService.fetchData(endpoint);
        return ResponseEntity.ok(data);
    }
}