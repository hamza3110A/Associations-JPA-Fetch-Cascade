package tn.esprit.zouitenhamza4iet_i4.Domaine;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEmploye;
    private String nom;
    private String prenom;
    @Enumerated(EnumType.STRING)
    private RoleEmploye role;

    // Employe * --- 1 Agence (cote proprietaire : FK agence_id_agence)
    @ManyToOne(fetch = FetchType.LAZY)
    private Agence agence;
}
