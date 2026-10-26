package tn.esprit.zouitenhamza4iet_i4.WebController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.zouitenhamza4iet_i4.Domaine.Vehicule;
import tn.esprit.zouitenhamza4iet_i4.Services.IVehiculeServices;

import java.util.List;

@RestController
@RequestMapping("vehicules")
@RequiredArgsConstructor
public class vehiiculeRestController {

    private final IVehiculeServices vehiculeService;

    @PostMapping("add")
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("{id}")
    public Vehicule findById(@PathVariable("id") Long id) {
        return vehiculeService.findById(id);
    }

    @PutMapping("{id}")
    public Vehicule update(@PathVariable("id") Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        vehiculeService.deleteById(id);
    }
}
