package tn.esprit.zouitenhamza4iet_i4.Services;

import tn.esprit.zouitenhamza4iet_i4.Domaine.Paiement;

import java.util.List;

/**
 * Lecture seule : un paiement est créé, modifié ou retiré uniquement via son Contrat
 * (composition Contrat <>--- Paiement, cascade ALL + orphanRemoval).
 */
public interface IPaiementService {

    Paiement findById(Long id);

    List<Paiement> findAll();
}
