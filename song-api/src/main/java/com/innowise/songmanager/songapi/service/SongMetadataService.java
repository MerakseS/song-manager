package com.innowise.songmanager.songapi.service;

import java.util.List;

import com.innowise.songmanager.contractapi.entity.SongMetadata;

public interface SongMetadataService {

    void create(SongMetadata songMetadata);

    List<SongMetadata> getAll();

    SongMetadata get(String id);

    void delete(String id, String token);
}
