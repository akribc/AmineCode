package ma.octo.assignement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class VersementDto {
    @JsonProperty("emetteur")
    private String nomPrenomEmetteur;
    private String rib;
    private String motif;
    private BigDecimal montant;
    private Date date;
}
