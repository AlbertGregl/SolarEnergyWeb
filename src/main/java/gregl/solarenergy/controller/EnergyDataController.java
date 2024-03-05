package gregl.solarenergy.controller;

import gregl.solarenergy.model.EnergyDataList;
import gregl.solarenergy.service.EnergyDataService;
import gregl.solarenergy.validationutil.XmlXsdUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@AllArgsConstructor
@RequestMapping("/mvc/energydata")
public class EnergyDataController {

    private final EnergyDataService energyDataService;

    @GetMapping("byYearMonth.html")
    public String fetchApiDataByYearMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month, Model model) {
        String endpoint = "/api/data/" + year + "/" + month;
        String xmlData = energyDataService.fetchData(endpoint);

        try {
            EnergyDataList data = XmlXsdUtil.unmarshal(xmlData, EnergyDataList.class);
            model.addAttribute("energyDataList", data.getEnergyData());
            return "dataTableView";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse and validate XML: " + e.getMessage());
            return "errorView";
        }
    }
}