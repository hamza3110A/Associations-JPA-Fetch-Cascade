package tn.esprit.zouitenhamza4iet_i4.WebController;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.zouitenhamza4iet_i4.Domaine.Vehicule;
import tn.esprit.zouitenhamza4iet_i4.Services.IVehiculeServices;
@RestController
@RequestMapping("vehicules")
@RequiredArgsConstructor
public class vehiiculeRestController
{



        private  final IVehiculeServices iVehiculeServices;



        @PostMapping("add")
        Vehicule create(@RequestBody Vehicule vehicule){
            return iVehiculeServices.add(vehicule);
        }











}
