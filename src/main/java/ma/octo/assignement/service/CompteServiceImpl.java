package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.repository.CompteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CompteServiceImpl implements CompteService {

    private CompteRepository compteRepository;

    public CompteServiceImpl(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    public List<Compte> loadAllCompte() {
        return compteRepository.findAll();
    }

    @Override
    public Compte getCompte(String nrCompte) {
        return compteRepository.findByNrCompte(nrCompte);
    }

    @Override
    public Compte getCompteByRib(String rib) {
        return compteRepository.findByRib(rib);
    }

    @Override
    public void debit(Compte compte, BigDecimal montant) {
        compte.debit(montant);
        compteRepository.save(compte);
    }

    @Override
    public void credit(Compte compte, BigDecimal montant) {
        compte.credit(montant);
        compteRepository.save(compte);
    }
}
