package com.innowise.songapi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.contractapi.entity.Artist;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {

    Optional<Artist> findArtistByName(String name);
}
