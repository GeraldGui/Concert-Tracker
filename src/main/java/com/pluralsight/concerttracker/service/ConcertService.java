package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.ArtistRepository;
import com.pluralsight.concerttracker.data.ConcertRepository;
import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.data.VenueRepository;
import com.pluralsight.concerttracker.model.Artist;
import com.pluralsight.concerttracker.model.Concert;
import com.pluralsight.concerttracker.model.Promoter;
import com.pluralsight.concerttracker.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final PromoterRepository promoterRepository;

    @Autowired
    public ConcertService(ConcertRepository concertRepository, VenueRepository venueRepository,
                          ArtistRepository artistRepository, PromoterRepository promoterRepository) {
        this.concertRepository = concertRepository;
        this.venueRepository = venueRepository;
        this.artistRepository = artistRepository;
        this.promoterRepository = promoterRepository;
    }

    public List<Concert> findAll() {
        return concertRepository.findAll();
    }

    public Concert findByIdOrThrow(long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No concert with id " + id));
    }

    public Concert addConcert(int year, double ticketPrice, int ticketsSold,
                              long artistId, long venueId, long promoterId) {
        if (ticketPrice < 0) {
            throw new InvalidInputException("Ticket price cannot be negative.");
        }
        if (ticketsSold < 0) {
            throw new InvalidInputException("Tickets sold cannot be negative.");
        }

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NotFoundException("No artist with id " + artistId));
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("No venue with id " + venueId));
        Promoter promoter = promoterRepository.findById(promoterId)
                .orElseThrow(() -> new NotFoundException("No promoter with id " + promoterId));

        if (ticketsSold > venue.getCapacity()) {
            throw new InvalidInputException("Tickets sold (" + ticketsSold
                    + ") cannot exceed venue capacity (" + venue.getCapacity() + ").");
        }

        return concertRepository.save(new Concert(year, ticketPrice, ticketsSold, artist, venue, promoter));
    }

    public Concert updatePrice(long id, double newPrice) {
        if (newPrice < 0) {
            throw new InvalidInputException("Ticket price cannot be negative.");
        }
        Concert concert = findByIdOrThrow(id);
        concert.setTicketPrice(newPrice);
        return concertRepository.save(concert);
    }

    public Concert updateTicketsSold(long id, int newTicketsSold) {
        if (newTicketsSold < 0) {
            throw new InvalidInputException("Tickets sold cannot be negative.");
        }
        Concert concert = findByIdOrThrow(id);
        if (newTicketsSold > concert.getVenue().getCapacity()) {
            throw new InvalidInputException("Tickets sold (" + newTicketsSold
                    + ") cannot exceed venue capacity (" + concert.getVenue().getCapacity() + ").");
        }
        concert.setTicketsSold(newTicketsSold);
        return concertRepository.save(concert);
    }

    public void deleteConcert(long id) {
        if (!concertRepository.existsById(id)) {
            throw new NotFoundException("No concert with id " + id);
        }
        concertRepository.deleteById(id);
    }

    public boolean isEmpty() {
        return concertRepository.count() == 0;
    }

    public void seedIfEmpty() {
        if (concertRepository.count() > 0) {
            return;
        }

        Venue msg = venueRepository.save(new Venue("Madison Square Garden", "New York", 20000));
        Venue wells = venueRepository.save(new Venue("Wells Fargo Center", "Philadelphia", 19500));
        Venue redRocks = venueRepository.save(new Venue("Red Rocks Amphitheatre", "Morrison", 9500));
        Venue tdGarden = venueRepository.save(new Venue("TD Garden", "Boston", 19580));

        Artist drake = artistRepository.save(new Artist("Drake", "Hip-Hop"));
        Artist beyonce = artistRepository.save(new Artist("Beyonce", "Pop"));
        Artist metallica = artistRepository.save(new Artist("Metallica", "Rock"));
        Artist daftPunk = artistRepository.save(new Artist("Daft Punk", "Electronic"));

        Promoter liveNation = promoterRepository.save(new Promoter("Live Nation"));
        Promoter aeg = promoterRepository.save(new Promoter("AEG Presents"));

        concertRepository.save(new Concert(2023, 150.00, 18000, drake, msg, liveNation));
        concertRepository.save(new Concert(2023, 200.00, 19000, beyonce, wells, liveNation));
        concertRepository.save(new Concert(2024, 175.50, 9000, metallica, redRocks, aeg));
        concertRepository.save(new Concert(2024, 120.00, 19500, daftPunk, tdGarden, aeg));
        concertRepository.save(new Concert(2024, 160.00, 17500, drake, wells, liveNation));
        concertRepository.save(new Concert(2025, 210.00, 19580, beyonce, tdGarden, aeg));
        concertRepository.save(new Concert(2025, 99.99, 9500, metallica, redRocks, liveNation));
        concertRepository.save(new Concert(2025, 130.00, 20000, daftPunk, msg, aeg));
        concertRepository.save(new Concert(2026, 145.00, 18500, drake, redRocks, liveNation));
        concertRepository.save(new Concert(2026, 185.00, 19000, beyonce, msg, aeg));
        concertRepository.save(new Concert(2026, 110.00, 19580, metallica, tdGarden, liveNation));
        concertRepository.save(new Concert(2026, 155.00, 19500, daftPunk, wells, aeg));

        System.out.println("Seed data loaded: 4 venues, 4 artists, 2 promoters, 12 concerts.");
    }
}