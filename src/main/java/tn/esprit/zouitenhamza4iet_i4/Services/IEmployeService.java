package tn.esprit.zouitenhamza4iet_i4.Services;

import tn.esprit.zouitenhamza4iet_i4.Domaine.Employe;

import java.util.List;

public interface IEmployeService {

    Employe create(Employe employe);

    Employe findById(Long id);

    List<Employe> findAll();

    Employe update(Long id, Employe employe);

    void deleteById(Long id);
}
