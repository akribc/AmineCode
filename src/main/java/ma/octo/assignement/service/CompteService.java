package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;

import java.math.BigDecimal;
import java.util.List;

public interface CompteService {
    List<Compte> loadAllCompte();
    Compte getCompte(String nrCompte);
    Compte getCompteByRib(String rib);
    void debit(Compte compte, BigDecimal montant);
    void credit(Compte compte, BigDecimal montant);
}
