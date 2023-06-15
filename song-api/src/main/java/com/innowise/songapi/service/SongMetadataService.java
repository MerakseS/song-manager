package com.innowise.songapi.service;

import java.util.List;

import com.innowise.contractapi.entity.SongMetadata;

public interface SongMetadataService {

    void create(SongMetadata songMetadata);

    List<SongMetadata> getAll();

    SongMetadata get(String id);

    void delete(String id, String token);
}
