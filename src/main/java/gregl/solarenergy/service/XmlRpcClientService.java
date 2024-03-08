package gregl.solarenergy.service;

import java.util.List;

public interface XmlRpcClientService {
    public String getTemperature(String city);
    public List<String> getCityNames();
}
