package tn.esprit.zouitenhamza4iet_i4.Services.Impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.zouitenhamza4iet_i4.Domaine.Vehicule;
import tn.esprit.zouitenhamza4iet_i4.Repository.IVehiculeRepository;
import tn.esprit.zouitenhamza4iet_i4.Services.IVehiculeServices;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeServices {
    private final IVehiculeRepository iVehiculeRepository;


    @Override
    public Vehicule add(Vehicule vehicule) {
        return iVehiculeRepository.save(vehicule);
    }

    @Override
    public void deleteById(Long id) {

    }
    @Override
    public Vehicule getById(Long id) {
        return iVehiculeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("vehicule not found"));

    }
        @Override
    public List<Vehicule> findAll() {
        return List.of();
    }
}
