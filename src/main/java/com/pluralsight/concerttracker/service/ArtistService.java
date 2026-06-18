package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.ArtistRepository;
import com.pluralsight.concerttracker.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Artist findByIdOrThrow(long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No artist with id " + id));
    }

    public Artist addArtist(String name, String genre) {
        return artistRepository.save(new Artist(name, genre));
    }

    public Artist updateGenre(long id, String newGenre) {
        Artist artist = findByIdOrThrow(id);
        artist.setGenre(newGenre);
        return artistRepository.save(artist);
    }

    public void deleteArtist(long id) {
        if (!artistRepository.existsById(id)) {
            throw new NotFoundException("No artist with id " + id);
        }
        artistRepository.deleteById(id);
    }

    public List<Artist> byGenre(String genre) {
        return artistRepository.findByGenreIgnoreCase(genre);
    }

    public List<Artist> byName(String name) {
        return artistRepository.findByNameContainingIgnoreCase(name);
    }

    public boolean isEmpty() {
        return artistRepository.count() == 0;
    }
}