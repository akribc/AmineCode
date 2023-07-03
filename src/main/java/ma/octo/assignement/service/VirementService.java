package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;

import java.math.BigDecimal;
import java.util.List;

public interface VirementService {
    List<Virement> loadAllVirement();
    void virement(Compte emetteur, Compte beneficiaire, BigDecimal montant, String motif)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException;
}
