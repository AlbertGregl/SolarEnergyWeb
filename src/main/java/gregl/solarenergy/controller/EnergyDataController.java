package gregl.solarenergy.controller;

import gregl.solarenergy.model.EnergyData;
import gregl.solarenergy.model.EnergyDataList;
import gregl.solarenergy.service.EnergyDataService;
import gregl.solarenergy.service.RngValidationService;
import gregl.solarenergy.validationutil.XmlXsdUtil;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;


@Controller
@AllArgsConstructor
@RequestMapping("/mvc/energydata")
public class EnergyDataController {

    private final EnergyDataService energyDataService;
    private final RngValidationService rngValidationService;


    @GetMapping("byYearMonth.html")
    public String fetchApiDataByYearMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month, Model model) {
        String endpoint = "/api/data/" + year + "/" + month;
        try {
            String xmlData = energyDataService.fetchData(endpoint);

            // unmarshalling the data from XML & validating it against the XSD
            EnergyDataList data = XmlXsdUtil.unmarshal(xmlData, EnergyDataList.class);

            model.addAttribute("energyDataList", data.getEnergyData());
            return "dataTableView";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse and validate XML: " + e.getMessage());
            return "errorView";
        }
    }

    @PostMapping("/add.html")
    public String addEnergyData(@ModelAttribute EnergyData energyData, RedirectAttributes redirectAttributes) {
        try {
            // TASK 1: marshalling the data to XML & validating it against the XSD
            energyData.setDtm(energyData.getDtmAsInstant());
            String xmlData = XmlXsdUtil.marshal(energyData);

            // TASK 2: validating the XML with the RNG schema
            boolean isValid = rngValidationService.validateRng(xmlData);
            if (!isValid) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Invalid energy data.");
                return "redirect:/";
            }

            energyDataService.addEnergyData(xmlData);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Energy data added successfully!");
            return "redirect:/";
        } catch (JAXBException | IOException | SAXException e) {
            return "errorView";
        }
    }
}