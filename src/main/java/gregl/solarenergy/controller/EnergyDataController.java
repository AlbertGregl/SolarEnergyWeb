package gregl.solarenergy.controller;

import gregl.solarenergy.model.EnergyData;
import gregl.solarenergy.model.EnergyDataList;
import gregl.solarenergy.model.GetEnergyDataByYearAndMonthResponse;
import gregl.solarenergy.validationutil.XmlXsdUtil;
import gregl.solarenergy.service.EnergyDataService;
import gregl.solarenergy.service.RngValidationService;


import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;
import java.io.IOException;




@Controller
@AllArgsConstructor
@RequestMapping("/mvc/energydata")
public class EnergyDataController {

    private final EnergyDataService energyDataService;
    private final RngValidationService rngValidationService;


    @GetMapping("byYearMonth.html")
    public String fetchApiDataByYearMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month, Model model) {
        try {
            // Task 4: Fetching data from the SOAP service
            String xmlData = energyDataService.fetchDataFromSoapService(year, month);

            // Task 5: Parsing and validating the XML using JAXB
            GetEnergyDataByYearAndMonthResponse response = XmlXsdUtil.unmarshalEnergyData(xmlData);
            EnergyDataList energyDataList = response.getEnergyDataList();

            // Task 7: Rendering the data in a table
            model.addAttribute("energyDataList", energyDataList.getEnergyData());
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

            // Task 7: Adding the data to the database
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