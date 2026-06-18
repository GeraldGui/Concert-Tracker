package com.pluralsight.concerttracker;

import com.pluralsight.concerttracker.model.Artist;
import com.pluralsight.concerttracker.model.Concert;
import com.pluralsight.concerttracker.model.Promoter;
import com.pluralsight.concerttracker.model.Venue;
import com.pluralsight.concerttracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConcertTrackerRunner implements CommandLineRunner {

    private final ConcertService concertService;
    private final VenueService venueService;
    private final ArtistService artistService;
    private final PromoterService promoterService;

    @Autowired
    public ConcertTrackerRunner(ConcertService concertService, VenueService venueService,
                                ArtistService artistService, PromoterService promoterService) {
        this.concertService = concertService;
        this.venueService = venueService;
        this.artistService = artistService;
        this.promoterService = promoterService;
    }

    @Override
    public void run(String... args) {
        concertService.seedIfEmpty();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Concerts");
            System.out.println("2) Search concerts");
            System.out.println("3) Artists");
            System.out.println("4) Venues");
            System.out.println("5) Promoters");
            System.out.println("6) Reports");
            System.out.println("0) Quit");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 -> concertsMenu(scanner);
                case 2 -> System.out.println("Coming in Phase 3.");
                case 3 -> artistsMenu(scanner);
                case 4 -> venuesMenu(scanner);
                case 5 -> promotersMenu(scanner);
                case 6 -> System.out.println("Coming in Phase 4.");
                case 0 -> running = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    // ================= CONCERTS =================

    private void concertsMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Concerts ---");
            System.out.println("1) List all concerts");
            System.out.println("2) View concert by id");
            System.out.println("3) Add concert");
            System.out.println("4) Update ticket price");
            System.out.println("5) Update tickets sold");
            System.out.println("6) Delete concert");
            System.out.println("0) Back");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 -> listAllConcerts();
                case 2 -> viewConcertById(scanner);
                case 3 -> addConcert(scanner);
                case 4 -> updateConcertPrice(scanner);
                case 5 -> updateConcertTicketsSold(scanner);
                case 6 -> deleteConcert(scanner);
                case 0 -> inMenu = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void printConcert(Concert c) {
        System.out.printf("ID: %d | %s at %s (%s) | Year: %d | Price: $%.2f | Sold: %d/%d%n",
                c.getId(), c.getArtist().getName(), c.getVenue().getName(), c.getVenue().getCity(),
                c.getConcertYear(), c.getTicketPrice(), c.getTicketsSold(), c.getVenue().getCapacity());
    }

    private void listAllConcerts() {
        List<Concert> concerts = concertService.findAll();
        if (concerts.isEmpty()) {
            System.out.println("No concerts found.");
            return;
        }
        concerts.forEach(this::printConcert);
    }

    private void viewConcertById(Scanner scanner) {
        System.out.print("Concert id: ");
        long id = scanner.nextLong();
        try {
            Concert concert = concertService.findByIdOrThrow(id);
            printConcert(concert);
            System.out.println("Promoter: " + concert.getPromoter().getName());
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addConcert(Scanner scanner) {
        try {
            System.out.println("Choose an artist:");
            artistService.findAll().forEach(a -> System.out.println(a.getId() + " - " + a.getName()));
            System.out.print("Artist id: ");
            long artistId = scanner.nextLong();

            System.out.println("Choose a venue:");
            venueService.findAll().forEach(v ->
                    System.out.println(v.getId() + " - " + v.getName() + " (" + v.getCity() + ")"));
            System.out.print("Venue id: ");
            long venueId = scanner.nextLong();

            System.out.println("Choose a promoter:");
            promoterService.findAll().forEach(p -> System.out.println(p.getId() + " - " + p.getName()));
            System.out.print("Promoter id: ");
            long promoterId = scanner.nextLong();

            System.out.print("Year: ");
            int year = scanner.nextInt();
            System.out.print("Ticket price: ");
            double price = scanner.nextDouble();
            System.out.print("Tickets sold: ");
            int ticketsSold = scanner.nextInt();

            concertService.addConcert(year, price, ticketsSold, artistId, venueId, promoterId);
            System.out.println("Added concert!");
        } catch (NotFoundException | InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateConcertPrice(Scanner scanner) {
        System.out.print("Concert id: ");
        long id = scanner.nextLong();
        System.out.print("New price: ");
        double newPrice = scanner.nextDouble();
        try {
            concertService.updatePrice(id, newPrice);
            System.out.println("Updated price!");
        } catch (NotFoundException | InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateConcertTicketsSold(Scanner scanner) {
        System.out.print("Concert id: ");
        long id = scanner.nextLong();
        System.out.print("New tickets sold: ");
        int newTicketsSold = scanner.nextInt();
        try {
            concertService.updateTicketsSold(id, newTicketsSold);
            System.out.println("Updated tickets sold!");
        } catch (NotFoundException | InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteConcert(Scanner scanner) {
        System.out.print("Concert id: ");
        long id = scanner.nextLong();
        try {
            concertService.deleteConcert(id);
            System.out.println("Deleted concert.");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= VENUES =================

    private void venuesMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Venues ---");
            System.out.println("1) List all venues");
            System.out.println("2) Add venue");
            System.out.println("3) Update capacity");
            System.out.println("4) Delete venue");
            System.out.println("5) Find by city");
            System.out.println("6) Find by name");
            System.out.println("7) Find by minimum capacity");
            System.out.println("0) Back");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 -> listAllVenues();
                case 2 -> addVenue(scanner);
                case 3 -> updateVenueCapacity(scanner);
                case 4 -> deleteVenue(scanner);
                case 5 -> findVenuesByCity(scanner);
                case 6 -> findVenuesByName(scanner);
                case 7 -> findVenuesByMinCapacity(scanner);
                case 0 -> inMenu = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void printVenue(Venue v) {
        System.out.printf("%d - %s | %s | capacity %d%n", v.getId(), v.getName(), v.getCity(), v.getCapacity());
    }

    private void listAllVenues() {
        List<Venue> venues = venueService.findAll();
        if (venues.isEmpty()) {
            System.out.println("No venues found.");
            return;
        }
        venues.forEach(this::printVenue);
    }

    private void addVenue(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Capacity: ");
        int capacity = scanner.nextInt();
        try {
            venueService.addVenue(name, city, capacity);
            System.out.println("Added venue!");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateVenueCapacity(Scanner scanner) {
        System.out.print("Venue id: ");
        long id = scanner.nextLong();
        System.out.print("New capacity: ");
        int newCapacity = scanner.nextInt();
        try {
            venueService.updateCapacity(id, newCapacity);
            System.out.println("Updated capacity!");
        } catch (NotFoundException | InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteVenue(Scanner scanner) {
        System.out.print("Venue id: ");
        long id = scanner.nextLong();
        try {
            venueService.deleteVenue(id);
            System.out.println("Deleted venue.");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            System.out.println("Cannot delete - this venue is linked to existing concerts.");
        }
    }

    private void findVenuesByCity(Scanner scanner) {
        scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        List<Venue> results = venueService.byCity(city);
        if (results.isEmpty()) {
            System.out.println("No venues found in that city.");
            return;
        }
        results.forEach(this::printVenue);
    }

    private void findVenuesByName(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Name contains: ");
        String name = scanner.nextLine();
        List<Venue> results = venueService.byName(name);
        if (results.isEmpty()) {
            System.out.println("No venues matched.");
            return;
        }
        results.forEach(this::printVenue);
    }

    private void findVenuesByMinCapacity(Scanner scanner) {
        System.out.print("Minimum capacity: ");
        int min = scanner.nextInt();
        List<Venue> results = venueService.byMinCapacity(min);
        if (results.isEmpty()) {
            System.out.println("No venues matched.");
            return;
        }
        results.forEach(this::printVenue);
    }

    // ================= ARTISTS =================

    private void artistsMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Artists ---");
            System.out.println("1) List all artists");
            System.out.println("2) Add artist");
            System.out.println("3) Update genre");
            System.out.println("4) Delete artist");
            System.out.println("5) Find by genre");
            System.out.println("6) Find by name");
            System.out.println("0) Back");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 -> listAllArtists();
                case 2 -> addArtist(scanner);
                case 3 -> updateArtistGenre(scanner);
                case 4 -> deleteArtist(scanner);
                case 5 -> findArtistsByGenre(scanner);
                case 6 -> findArtistsByName(scanner);
                case 0 -> inMenu = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void printArtist(Artist a) {
        System.out.printf("%d - %s (%s)%n", a.getId(), a.getName(), a.getGenre());
    }

    private void listAllArtists() {
        List<Artist> artists = artistService.findAll();
        if (artists.isEmpty()) {
            System.out.println("No artists found.");
            return;
        }
        artists.forEach(this::printArtist);
    }

    private void addArtist(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        artistService.addArtist(name, genre);
        System.out.println("Added artist!");
    }

    private void updateArtistGenre(Scanner scanner) {
        System.out.print("Artist id: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("New genre: ");
        String genre = scanner.nextLine();
        try {
            artistService.updateGenre(id, genre);
            System.out.println("Updated genre!");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteArtist(Scanner scanner) {
        System.out.print("Artist id: ");
        long id = scanner.nextLong();
        try {
            artistService.deleteArtist(id);
            System.out.println("Deleted artist.");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            System.out.println("Cannot delete - this artist is linked to existing concerts.");
        }
    }

    private void findArtistsByGenre(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        List<Artist> results = artistService.byGenre(genre);
        if (results.isEmpty()) {
            System.out.println("No artists matched.");
            return;
        }
        results.forEach(this::printArtist);
    }

    private void findArtistsByName(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Name contains: ");
        String name = scanner.nextLine();
        List<Artist> results = artistService.byName(name);
        if (results.isEmpty()) {
            System.out.println("No artists matched.");
            return;
        }
        results.forEach(this::printArtist);
    }

    // ================= PROMOTERS =================

    private void promotersMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Promoters ---");
            System.out.println("1) List all promoters");
            System.out.println("2) Add promoter");
            System.out.println("3) Delete promoter");
            System.out.println("4) Find by name");
            System.out.println("0) Back");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 -> listAllPromoters();
                case 2 -> addPromoter(scanner);
                case 3 -> deletePromoter(scanner);
                case 4 -> findPromotersByName(scanner);
                case 0 -> inMenu = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void printPromoter(Promoter p) {
        System.out.printf("%d - %s%n", p.getId(), p.getName());
    }

    private void listAllPromoters() {
        List<Promoter> promoters = promoterService.findAll();
        if (promoters.isEmpty()) {
            System.out.println("No promoters found.");
            return;
        }
        promoters.forEach(this::printPromoter);
    }

    private void addPromoter(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        promoterService.addPromoter(name);
        System.out.println("Added promoter!");
    }

    private void deletePromoter(Scanner scanner) {
        System.out.print("Promoter id: ");
        long id = scanner.nextLong();
        try {
            promoterService.deletePromoter(id);
            System.out.println("Deleted promoter.");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            System.out.println("Cannot delete - this promoter is linked to existing concerts.");
        }
    }

    private void findPromotersByName(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Name contains: ");
        String name = scanner.nextLine();
        List<Promoter> results = promoterService.byName(name);
        if (results.isEmpty()) {
            System.out.println("No promoters matched.");
            return;
        }
        results.forEach(this::printPromoter);
    }
}