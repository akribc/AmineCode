package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;

import java.math.BigDecimal;
import java.util.List;

public interface VersementService {
    List<Versement> loadAllVersement();
    void versement(String emetteur, Compte beneficiaire, BigDecimal montant, String motif)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException;
}
