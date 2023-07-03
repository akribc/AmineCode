package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VersementDto;
import ma.octo.assignement.dto.VirementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.mapper.VersementMapper;
import ma.octo.assignement.mapper.VirementMapper;
import ma.octo.assignement.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController(value = "virements")
public class VirementController {

    private final CompteService compteService;
    private final VirementService virementService;
    private final AuditService auditService;
    private final UtilisateurService utilisateurService;
    private final VersementService versementService;

    public VirementController(CompteService compteService, VirementService virementService,
                              UtilisateurService utilisateurService, AuditService auditService, VersementService versementService) {
        this.compteService = compteService;
        this.virementService = virementService;
        this.utilisateurService = utilisateurService;
        this.auditService = auditService;
        this.versementService = versementService;
    }

    @GetMapping("lister_virements")
    @PreAuthorize("hasAuthority('USER')")
    public List<VirementDto> loadAllVirement() {
        List<Virement> virements = virementService.loadAllVirement();
        return virements.stream()
                .map(VirementMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("lister_versements")
    @PreAuthorize("hasAuthority('USER')")
    public List<VersementDto> loadAllVersement() {
        List<Versement> versements = versementService.loadAllVersement();
        return versements.stream()
                .map(VersementMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("lister_comptes")
    @PreAuthorize("hasAuthority('USER')")
    public List<Compte> loadAllCompte() {
        return compteService.loadAllCompte();
    }

    @GetMapping("lister_utilisateurs")
    @PreAuthorize("hasAuthority('USER')")
    public List<Utilisateur> loadAllUtilisateur() {
        return utilisateurService.loadAllUtilisateur();
    }

    @PostMapping("executer_virement")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createVirement(@RequestBody VirementDto virementDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {

        Compte emetteur = compteService.getCompte(virementDto.getNrCompteEmetteur());
        Compte beneficiaire = compteService.getCompte(virementDto.getNrCompteBeneficiaire());

        virementService.virement(emetteur, beneficiaire, virementDto.getMontantVirement(), virementDto.getMotif());

        auditService.auditVirement("Virement depuis " + virementDto.getNrCompteEmetteur() + " vers " + virementDto
                .getNrCompteBeneficiaire() + " d'un montant de " + virementDto.getMontantVirement());
    }

    @PostMapping("executer_versement")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createVersement(@RequestBody VersementDto versementDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {

        String emetteur = versementDto.getNomPrenomEmetteur();
        Compte beneficiaire = compteService.getCompteByRib(versementDto.getRib());

        versementService.versement(emetteur, beneficiaire, versementDto.getMontant(), versementDto.getMotif());


        auditService.auditVersement("Versement depuis " + versementDto.getNomPrenomEmetteur() + " vers " + versementDto
                .getRib() + " d'un montant de " + versementDto.getMontant());
    }
}
