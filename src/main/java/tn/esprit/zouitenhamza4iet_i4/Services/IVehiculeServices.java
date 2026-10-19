package tn.esprit.zouitenhamza4iet_i4.Services;

import tn.esprit.zouitenhamza4iet_i4.Domaine.Vehicule;

import java.util.List;

public interface IVehiculeServices {
    Vehicule getById(Long id);
    Vehicule add(Vehicule vehicule);
    void deleteById(Long id);
    List<Vehicule> findAll();



}