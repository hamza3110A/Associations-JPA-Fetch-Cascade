package tn.esprit.zouitenhamza4iet_i4.Domaine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // Reservation * --- 1 Client (cote proprietaire : FK client_id_client)
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    // Reservation * ---> 1 Vehicule (unidirectionnelle, cote proprietaire : FK vehicule_id_vehicule)
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicule vehicule;

    // Reservation 1 --- 1 Contrat (cote inverse) : ALL
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contrat contrat;
}
