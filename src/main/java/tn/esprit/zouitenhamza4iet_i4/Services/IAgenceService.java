package tn.esprit.zouitenhamza4iet_i4.Services;

import tn.esprit.zouitenhamza4iet_i4.Domaine.Agence;

import java.util.List;

public interface IAgenceService {

    Agence create(Agence agence);

    Agence findById(Long id);

    List<Agence> findAll();

    Agence update(Long id, Agence agence);

    void deleteById(Long id);
}
