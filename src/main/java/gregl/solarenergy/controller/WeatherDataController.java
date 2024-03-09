package gregl.solarenergy.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import gregl.solarenergy.service.XmlRpcClientService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
@RequestMapping("/mvc/weather")
public class WeatherDataController {

    // Task 5: XML-RPC client service for fetching the DHMZ temperature
    private final XmlRpcClientService xmlRpcClientService;

    @GetMapping("/temperature.html")
    public String showTemperature(@RequestParam String city, Model model) {
        String temperature = xmlRpcClientService.getTemperature(city);

        // Task 7: rendering the temperature in a view
        model.addAttribute("city", city);
        model.addAttribute("temperature", temperature);
        return "temperatureView";
    }
}
