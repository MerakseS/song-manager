package com.innowise.songapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.contractapi.entity.SongMetadata;

@Repository
public interface SongMetadataRepository extends MongoRepository<SongMetadata, String> {

}
