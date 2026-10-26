package tn.esprit.zouitenhamza4iet_i4.Services;

import tn.esprit.zouitenhamza4iet_i4.Domaine.Vehicule;

import java.util.List;

public interface IVehiculeServices {

    Vehicule create(Vehicule vehicule);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);
}
