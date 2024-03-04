package gregl.solarenergy.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "energyDataList")
public class EnergyDataList {

    private List<EnergyData> energyData;

    @XmlElement(name = "energyData")
    public List<EnergyData> getEnergyData() {
        return energyData;
    }

    public void setEnergyData(List<EnergyData> energyData) {
        this.energyData = energyData;
    }
}
