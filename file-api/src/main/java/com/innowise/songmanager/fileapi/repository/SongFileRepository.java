package com.innowise.songmanager.fileapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.songmanager.contractapi.entity.SongFile;

@Repository
public interface SongFileRepository extends MongoRepository<SongFile, String> {

}
