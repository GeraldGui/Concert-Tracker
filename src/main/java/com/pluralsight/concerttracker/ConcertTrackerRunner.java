package com.pluralsight.concerttracker;

import com.pluralsight.concerttracker.model.Concert;
import com.pluralsight.concerttracker.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConcertTrackerRunner implements CommandLineRunner {

    private final ConcertService concertService;

    @Autowired
    public ConcertTrackerRunner(ConcertService concertService) {
        this.concertService = concertService;
    }

    @Override
    public void run(String... args) throws Exception {
        concertService.seedIfEmpty();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Concert Tracker ---");
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
                case 0 -> running = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void concertsMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Concerts ---");
            System.out.println("1) List all concerts");
            System.out.println("0) Back");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 -> listAllConcerts();
                case 0 -> inMenu = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void listAllConcerts() {
        List<Concert> concerts = concertService.findAll();
        if (concerts.isEmpty()) {
            System.out.println("No concerts found.");
            return;
        }
        for (Concert c : concerts) {
            System.out.printf("ID: %d | %s at %s (%s) | Year: %d | Price: $%.2f | Sold: %d%n",
                    c.getId(),
                    c.getArtist().getName(),
                    c.getVenue().getName(),
                    c.getVenue().getCity(),
                    c.getConcertYear(),
                    c.getTicketPrice(),
                    c.getTicketsSold());
        }
    }
}