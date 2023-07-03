package ma.octo.assignement.service;

import ma.octo.assignement.domain.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    List<Utilisateur> loadAllUtilisateur();
    Utilisateur loadUserByUsername(String username);
}
