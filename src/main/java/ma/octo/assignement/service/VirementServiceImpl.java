package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.VirementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VirementServiceImpl implements VirementService {

    private static final BigDecimal MONTANT_MAXIMAL = BigDecimal.valueOf(10000L);
    private static final BigDecimal MONTANT_MINIMAL = BigDecimal.TEN;
    private final Logger log = LoggerFactory.getLogger(VirementServiceImpl.class);
    private final VirementRepository virementRepository;
    private final CompteService compteService;

    public VirementServiceImpl(VirementRepository virementRepository, CompteService compteService) {
        this.virementRepository = virementRepository;
        this.compteService = compteService;
    }

    @Override
    public List<Virement> loadAllVirement() {
        return virementRepository.findAll();
    }

    private Virement create(Compte emetteur, Compte beneficiaire, BigDecimal montant, String motif) {
        Virement virement = new Virement();
        virement.setCompteEmetteur(emetteur);
        virement.setCompteBeneficiaire(beneficiaire);
        virement.setMontantVirement(montant);
        virement.setMotifVirement(motif);
        virement.setDateExecution(new Date());
        return virementRepository.save(virement);
    }

    @Override
    public void virement(Compte emetteur, Compte beneficiaire, BigDecimal montant, String motif)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {

        if (emetteur == null || beneficiaire == null) {
            log.error("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (montant == null) {
            log.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (montant.compareTo(BigDecimal.ZERO) == 0) {
            log.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (montant.compareTo(MONTANT_MINIMAL) < 0) {
            log.error("Montant minimal de virement non atteint");
            throw new TransactionException("Montant minimal de virement non atteint");
        } else if (montant.compareTo(MONTANT_MAXIMAL) > 0) {
            log.error("Montant maximal de virement dépassé");
            throw new TransactionException("Montant maximal de virement dépassé");
        }

        if (motif == null || motif.length() == 0) {
            log.error("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (emetteur.getSolde().compareTo(montant) < 0) {
            log.error("Solde insuffisant pour l'utilisateur");
            throw new SoldeDisponibleInsuffisantException("Solde insuffisant pour l'utilisateur");
        }

        compteService.debit(emetteur, montant);

        compteService.credit(beneficiaire, montant);

        create(emetteur, beneficiaire, montant, motif);
    }
}
