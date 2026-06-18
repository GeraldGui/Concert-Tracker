package com.pluralsight.concerttracker.data;

import com.pluralsight.concerttracker.model.Concert;
import com.pluralsight.concerttracker.report.ArtistConcertCount;
import com.pluralsight.concerttracker.report.VenueConcertCount;
import com.pluralsight.concerttracker.report.VenueRevenue;
import com.pluralsight.concerttracker.report.YearlyAveragePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE c.concertYear = :year")
    List<Concert> findByYear(@Param("year") int year);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.artist.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Concert> findByArtistNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.venue.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Concert> findByVenueNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.venue.city) = LOWER(:city)")
    List<Concert> findByVenueCity(@Param("city") String city);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice <= :maxPrice")
    List<Concert> findByMaxPrice(@Param("maxPrice") double maxPrice);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice BETWEEN :minPrice AND :maxPrice")
    List<Concert> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice <= :maxPrice AND c.concertYear >= :earliestYear")
    List<Concert> search(@Param("maxPrice") double maxPrice, @Param("earliestYear") int earliestYear);

    @Query("SELECT new com.pluralsight.concerttracker.report.VenueRevenue(v.name, SUM(c.ticketPrice * c.ticketsSold)) " +
            "FROM Concert c JOIN c.venue v GROUP BY v.name")
    List<VenueRevenue> revenuePerVenue();

    @Query("SELECT new com.pluralsight.concerttracker.report.VenueConcertCount(v.name, COUNT(c)) " +
            "FROM Concert c JOIN c.venue v GROUP BY v.name ORDER BY COUNT(c) DESC")
    List<VenueConcertCount> venueConcertCounts();

    @Query("SELECT new com.pluralsight.concerttracker.report.ArtistConcertCount(a.name, COUNT(c)) " +
            "FROM Concert c JOIN c.artist a GROUP BY a.name ORDER BY COUNT(c) DESC")
    List<ArtistConcertCount> artistConcertCounts();

    @Query("SELECT new com.pluralsight.concerttracker.report.YearlyAveragePrice(c.concertYear, AVG(c.ticketPrice)) " +
            "FROM Concert c GROUP BY c.concertYear ORDER BY c.concertYear")
    List<YearlyAveragePrice> averagePriceByYear();
}