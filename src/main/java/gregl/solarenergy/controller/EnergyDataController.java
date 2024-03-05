package gregl.solarenergy.controller;

import gregl.solarenergy.model.EnergyData;
import gregl.solarenergy.model.EnergyDataList;
import gregl.solarenergy.service.EnergyDataService;
import gregl.solarenergy.validationutil.XmlXsdUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.xml.bind.JAXBException;


@Controller
@AllArgsConstructor
@RequestMapping("/mvc/energydata")
public class EnergyDataController {

    private final EnergyDataService energyDataService;

    @GetMapping("byYearMonth.html")
    public String fetchApiDataByYearMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month, Model model) {
        String endpoint = "/api/data/" + year + "/" + month;
        try {
            String xmlData = energyDataService.fetchData(endpoint);
            EnergyDataList data = XmlXsdUtil.unmarshal(xmlData, EnergyDataList.class);
            model.addAttribute("energyDataList", data.getEnergyData());
            return "dataTableView";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse and validate XML: " + e.getMessage());
            return "errorView";
        }
    }

    @GetMapping("/add.html")
    public String showAddEnergyDataForm(Model model) {
        model.addAttribute("energyData", new EnergyData());
        return "addEnergyDataForm";
    }

    @PostMapping("/add.html")
    public String addEnergyData(@ModelAttribute EnergyData energyData, Model model) {
        try {
            energyData.setDtm(energyData.getDtmAsInstant());
            String xmlData = XmlXsdUtil.marshal(energyData);
            energyDataService.addEnergyData(xmlData);
            return "redirect:/#energy";
        } catch (JAXBException e) {
            model.addAttribute("error", "Failed to marshal XML: " + e.getMessage());
            return "errorView";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "errorView";
        }
    }

}