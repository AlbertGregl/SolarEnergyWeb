package gregl.solarenergy.controller;

import org.springframework.stereotype.Controller;
import gregl.solarenergy.service.XmlRpcClientService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WeatherDataController {

    private static final List<String> cityNames = new ArrayList<>();

    private final XmlRpcClientService xmlRpcClientService;

    public WeatherDataController(XmlRpcClientService xmlRpcClientService) {
        this.xmlRpcClientService = xmlRpcClientService;
        cityNames.addAll(xmlRpcClientService.getCityNames());
    }

    @GetMapping("/getTemperature.html")
    public String getTemperatureForm() {
        return "getTemperature";
    }

    @PostMapping("/getTemperature.html")
    public String showTemperature(@RequestParam String city, Model model) {
        String temperature = xmlRpcClientService.getTemperature(city);
        model.addAttribute("temperature", temperature);
        return "showTemperature";
    }
}
