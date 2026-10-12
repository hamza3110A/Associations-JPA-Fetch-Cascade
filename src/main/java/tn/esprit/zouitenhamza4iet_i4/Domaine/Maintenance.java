package tn.esprit.zouitenhamza4iet_i4.Domaine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMaintenance;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;

    // Maintenance * --- 1 Vehicule (cote proprietaire : FK vehicule_id_vehicule)
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicule vehicule;
}
