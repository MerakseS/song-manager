package com.innowise.songmanager.songapi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.songmanager.contractapi.entity.Album;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {

    Optional<Album> findByName(String name);

    default Album saveIfNotExists(Album album) {
        Optional<Album> optionalAlbum = findByName(album.getName());
        if (optionalAlbum.isPresent()) {
            album.setId(optionalAlbum.get().getId());
            return album;
        }
        else {
            return save(album);
        }
    }
}
