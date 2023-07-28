package com.innowise.songmanager.songapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.songmanager.contractapi.entity.Artist;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {

    Optional<Artist> findByName(String name);

    default Artist saveIfNotExists(Artist artist) {
        Optional<Artist> optionalArtist = findByName(artist.getName());
        if (optionalArtist.isPresent()) {
            artist.setId(optionalArtist.get().getId());
            return artist;
        }
        else {
            return save(artist);
        }
    }

    default List<Artist> saveAllIfNotExists(List<Artist> artists) {
        for (Artist artist : artists) {
            saveIfNotExists(artist);
        }

        return artists;
    }
}
