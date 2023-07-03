package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VirementDto;

public class VirementMapper {

    public static VirementDto map(Virement virement) {
        VirementDto virementDto = new VirementDto();
        virementDto.setNrCompteEmetteur(virement.getCompteEmetteur().getNrCompte());
        virementDto.setNrCompteBeneficiaire(virement.getCompteBeneficiaire().getNrCompte());
        virementDto.setMontantVirement(virement.getMontantVirement());
        virementDto.setMotif(virement.getMotifVirement());
        virementDto.setDate(virement.getDateExecution());
        return virementDto;
    }
}
