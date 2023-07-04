package com.innowise.songmanager.songapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.innowise.songmanager.contractapi.entity.SongMetadata;

@Repository
public interface SongMetadataRepository extends MongoRepository<SongMetadata, String> {

}
