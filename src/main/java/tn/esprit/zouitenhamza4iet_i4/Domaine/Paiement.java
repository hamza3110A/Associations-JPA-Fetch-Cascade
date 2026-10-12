package tn.esprit.zouitenhamza4iet_i4.Domaine;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPaiement;
    private BigDecimal montant;
    private LocalDate datePaiement;
    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    // Paiement * --- 1 Contrat (cote proprietaire : FK contrat_id_contrat)
    @ManyToOne(fetch = FetchType.LAZY)
    private Contrat contrat;
}
