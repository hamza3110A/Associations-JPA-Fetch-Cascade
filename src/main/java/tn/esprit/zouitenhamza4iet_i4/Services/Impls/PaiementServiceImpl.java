package tn.esprit.zouitenhamza4iet_i4.Services.Impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.zouitenhamza4iet_i4.Domaine.Paiement;
import tn.esprit.zouitenhamza4iet_i4.Repository.IPaiementRepository;
import tn.esprit.zouitenhamza4iet_i4.Services.IPaiementService;
import tn.esprit.zouitenhamza4iet_i4.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;

    @Override
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }
}
