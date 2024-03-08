package gregl.solarenergy.service;

public interface EnergyDataService {
    String fetchDataFromSoapService(int year, int month);
    void addEnergyData(String xmlData);

    String fetchToken();
}