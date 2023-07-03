package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.VersementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class VersementServiceImpl implements VersementService {
    private static final BigDecimal MONTANT_MAXIMAL = BigDecimal.valueOf(10000L);
    private static final BigDecimal MONTANT_MINIMAL = BigDecimal.TEN;
    private final Logger log = LoggerFactory.getLogger(VersementServiceImpl.class);
    private VersementRepository versementRepository;
    private final CompteService compteService;

    public VersementServiceImpl(VersementRepository versementRepository, CompteService compteService) {
        this.versementRepository = versementRepository;
        this.compteService = compteService;
    }

    private Versement create(String emetteur, Compte beneficiaire, BigDecimal montant, String motif) {
        Versement versement = new Versement();
        versement.setNomPrenomEmetteur(emetteur);
        versement.setCompteBeneficiaire(beneficiaire);
        versement.setMontantVirement(montant);
        versement.setMotifVersement(motif);
        versement.setDateExecution(new Date());
        return versementRepository.save(versement);
    }

    @Override
    public List<Versement> loadAllVersement() {
        return versementRepository.findAll();
    }

    @Override
    public void versement(String emetteur, Compte beneficiaire, BigDecimal montant, String motif)
            throws CompteNonExistantException, TransactionException {

        if (beneficiaire == null) {
            log.error("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (emetteur.trim().length() == 0) {
            log.error("nom et prénom d'emetteur vide");
            throw new TransactionException("nom et prénom d'emetteur vide");
        } else if (montant == null) {
            log.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (montant.compareTo(BigDecimal.ZERO) == 0) {
            log.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (montant.compareTo(MONTANT_MINIMAL) < 0) {
            log.error("Montant minimal de versement non atteint");
            throw new TransactionException("Montant minimal de versement non atteint");
        } else if (montant.compareTo(MONTANT_MAXIMAL) > 0) {
            log.error("Montant maximal de versement dépassé");
            throw new TransactionException("Montant maximal de versement dépassé");
        }

        if (motif == null || motif.length() == 0) {
            log.error("Motif vide");
            throw new TransactionException("Motif vide");
        }

        compteService.credit(beneficiaire, montant);

        create(emetteur, beneficiaire, montant, motif);
    }
}
