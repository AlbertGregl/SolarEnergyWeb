package gregl.solarenergy.controller;

import gregl.solarenergy.model.EnergyDataList;
import gregl.solarenergy.service.EnergyDataService;
import gregl.solarenergy.validationutil.XmlXsdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;


@Controller
public class EnergyDataController {

    private final EnergyDataService energyDataService;

    @Autowired
    public EnergyDataController(EnergyDataService energyDataService) {
        this.energyDataService = energyDataService;
    }

    @GetMapping("/api/data/{year}/{month}")
    public String fetchData(@PathVariable String year, @PathVariable String month, Model model) {
        String endpoint = "/api/data/" + year + "/" + month;
        String xmlData = energyDataService.fetchData(endpoint);

        try {
            EnergyDataList data = XmlXsdUtil.unmarshal(xmlData, EnergyDataList.class);
            model.addAttribute("energyDataList", data.getEnergyData());
            return "dataTableView :: energyDataTable";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse and validate XML: " + e.getMessage());
            return "errorView";
        }
    }
}