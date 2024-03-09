package gregl.solarenergy.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import gregl.solarenergy.service.XmlRpcClientService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
@RequestMapping("/mvc/weather")
public class WeatherDataController {

    private final XmlRpcClientService xmlRpcClientService;

    @PostMapping("/temperature.html")
    public String showTemperature(@RequestParam String city, Model model) {
        String temperature = xmlRpcClientService.getTemperature(city);
        model.addAttribute("temperature", temperature);
        return "showTemperature";
    }
}
