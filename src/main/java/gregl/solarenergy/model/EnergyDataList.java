package gregl.solarenergy.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class EnergyDataList {

    @XmlElement(name = "energyData", namespace = "http://gregl/soap/data.wsdl")
    private List<EnergyData> energyData;

    public List<EnergyData> getEnergyData() {
        return energyData;
    }

    public void setEnergyData(List<EnergyData> energyData) {
        this.energyData = energyData;
    }
}
