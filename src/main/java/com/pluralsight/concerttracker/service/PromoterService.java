package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.model.Promoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoterService {

    private final PromoterRepository promoterRepository;

    @Autowired
    public PromoterService(PromoterRepository promoterRepository) {
        this.promoterRepository = promoterRepository;
    }

    public List<Promoter> findAll() {
        return promoterRepository.findAll();
    }

    public Promoter findByIdOrThrow(long id) {
        return promoterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No promoter with id " + id));
    }

    public Promoter addPromoter(String name) {
        return promoterRepository.save(new Promoter(name));
    }

    public void deletePromoter(long id) {
        if (!promoterRepository.existsById(id)) {
            throw new NotFoundException("No promoter with id " + id);
        }
        promoterRepository.deleteById(id);
    }

    public List<Promoter> byName(String name) {
        return promoterRepository.findByNameContainingIgnoreCase(name);
    }

    public boolean isEmpty() {
        return promoterRepository.count() == 0;
    }
}