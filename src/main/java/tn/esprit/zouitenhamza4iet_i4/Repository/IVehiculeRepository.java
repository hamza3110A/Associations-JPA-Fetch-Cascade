package tn.esprit.zouitenhamza4iet_i4.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.zouitenhamza4iet_i4.Domaine.Vehicule;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
}
