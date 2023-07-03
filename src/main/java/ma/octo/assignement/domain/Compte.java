package ma.octo.assignement.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "COMPTE")
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 16, unique = true)
    private String nrCompte;

    private String rib;

    @Column(precision = 16, scale = 2)
    private BigDecimal solde;

    @ManyToOne()
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public void debit(BigDecimal montant) {
      setSolde(getSolde().subtract(montant));
    }

    public void credit(BigDecimal montant) {
      setSolde(getSolde().add(montant));
    }
}
