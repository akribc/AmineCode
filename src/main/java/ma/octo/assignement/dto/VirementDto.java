package ma.octo.assignement.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class VirementDto {
    private String nrCompteEmetteur;
    private String nrCompteBeneficiaire;
    private String motif;
    private BigDecimal montantVirement;
    private Date date;
}
