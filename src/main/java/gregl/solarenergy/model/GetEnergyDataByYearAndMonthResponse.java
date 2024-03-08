package gregl.solarenergy.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getEnergyDataByYearAndMonthResponse", namespace = "http://gregl/soap/data.wsdl")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetEnergyDataByYearAndMonthResponse {

    @XmlElement(name = "energyDataList", namespace = "http://gregl/soap/data.wsdl")
    private EnergyDataList energyDataList;

    public EnergyDataList getEnergyDataList() {
        return energyDataList;
    }

    public void setEnergyDataList(EnergyDataList energyDataList) {
        this.energyDataList = energyDataList;
    }
}
