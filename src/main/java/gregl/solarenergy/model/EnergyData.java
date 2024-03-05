package gregl.solarenergy.model;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@XmlRootElement(name = "energyData")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnergyData {

    private BigDecimal boa_MWh;
    private BigDecimal DA_Price;

    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String dtm;

    private Long id;
    private BigDecimal MIP;
    private BigDecimal SS_Price;
    private BigDecimal solar_MW;
    private BigDecimal solar_capacity_mwp;
    private BigDecimal solar_installedcapacity_mwp;
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
            // Handle or log the error appropriately
            return null;
        }
    }
}
