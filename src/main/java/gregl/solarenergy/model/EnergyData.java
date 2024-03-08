package gregl.solarenergy.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "energyData", namespace = "http://gregl/soap/data.wsdl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnergyData {

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal boa_MWh;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal DA_Price;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private String dtm;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private Long id;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal MIP;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal SS_Price;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal solar_MW;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal solar_capacity_mwp;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal solar_installedcapacity_mwp;

    @XmlElement(namespace = "http://gregl/soap/data.wsdl")
    private BigDecimal wind_MW;

    public EnergyData(String dtm, BigDecimal MIP, BigDecimal solar_MW, BigDecimal solar_capacity_mwp, BigDecimal solar_installedcapacity_mwp, BigDecimal wind_MW, BigDecimal SS_Price, BigDecimal boa_MWh, BigDecimal DA_Price) {
        this.dtm = dtm;
        this.MIP = MIP;
        this.solar_MW = solar_MW;
        this.solar_capacity_mwp = solar_capacity_mwp;
        this.solar_installedcapacity_mwp = solar_installedcapacity_mwp;
        this.wind_MW = wind_MW;
        this.SS_Price = SS_Price;
        this.boa_MWh = boa_MWh;
        this.DA_Price = DA_Price;
    }

    public String getDtmAsInstant() {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(this.dtm, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
            return instant.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
