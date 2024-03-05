package gregl.solarenergy.service;

public interface EnergyDataService {
    String fetchData(String endpoint);
    void addEnergyData(String xmlData);
}