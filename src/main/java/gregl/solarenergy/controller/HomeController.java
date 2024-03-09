package gregl.solarenergy.controller;

import gregl.solarenergy.model.EnergyData;
import gregl.solarenergy.service.XmlRpcClientService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private static final List<String> cityNames = new ArrayList<>();

    public HomeController(XmlRpcClientService xmlRpcClientService) {
        // eagerly load city names from the XML-RPC service
        cityNames.addAll(xmlRpcClientService.getCityNames());
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            model.addAttribute("profile", principal.getClaims());
        }
        model.addAttribute("energyData", new EnergyData());
        model.addAttribute("cityNames", cityNames);
        return "index";
    }

}