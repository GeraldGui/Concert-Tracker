package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.model.Promoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromoterService {

    private final PromoterRepository promoterRepository;

    @Autowired
    public PromoterService(PromoterRepository promoterRepository) {
        this.promoterRepository = promoterRepository;
    }

    public Promoter save(Promoter promoter) {
        return promoterRepository.save(promoter);
    }

    public List<Promoter> findAll() {
        return promoterRepository.findAll();
    }

    public Optional<Promoter> findById(Long id) {
        return promoterRepository.findById(id);
    }

    public void deleteById(Long id) {
        promoterRepository.deleteById(id);
    }

    public boolean isEmpty() {
        return promoterRepository.count() == 0;
    }
}