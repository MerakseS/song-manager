package com.innowise.fileapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.fileapi.entity.Song;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {

}
