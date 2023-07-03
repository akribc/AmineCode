package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.dto.VersementDto;

public class VersementMapper {
    public static VersementDto map(Versement versement) {
        VersementDto versementDto = new VersementDto();
        versementDto.setNomPrenomEmetteur(versement.getNomPrenomEmetteur());
        versementDto.setRib(versement.getCompteBeneficiaire().getRib());
        versementDto.setMontant(versement.getMontantVirement());
        versementDto.setMotif(versement.getMotifVersement());
        versementDto.setDate(versement.getDateExecution());
        return versementDto;
    }
}
