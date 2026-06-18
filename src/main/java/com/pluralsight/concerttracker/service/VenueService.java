package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.VenueRepository;
import com.pluralsight.concerttracker.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    public Venue findByIdOrThrow(long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No venue with id " + id));
    }

    public Venue addVenue(String name, String city, int capacity) {
        if (capacity <= 0) {
            throw new InvalidInputException("Capacity must be greater than zero.");
        }
        return venueRepository.save(new Venue(name, city, capacity));
    }

    public Venue updateCapacity(long id, int newCapacity) {
        if (newCapacity <= 0) {
            throw new InvalidInputException("Capacity must be greater than zero.");
        }
        Venue venue = findByIdOrThrow(id);
        venue.setCapacity(newCapacity);
        return venueRepository.save(venue);
    }

    public void deleteVenue(long id) {
        if (!venueRepository.existsById(id)) {
            throw new NotFoundException("No venue with id " + id);
        }
        venueRepository.deleteById(id);
    }

    public List<Venue> byCity(String city) {
        return venueRepository.findByCity(city);
    }

    public List<Venue> byName(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Venue> byMinCapacity(int minCapacity) {
        return venueRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    public boolean isEmpty() {
        return venueRepository.count() == 0;
    }
}