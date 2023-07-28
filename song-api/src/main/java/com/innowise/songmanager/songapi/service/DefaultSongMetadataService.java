package com.innowise.songmanager.songapi.service;

import java.util.List;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innowise.songmanager.contractapi.entity.SongMetadata;
import com.innowise.songmanager.contractapi.exception.impl.EntityNotFoundException;
import com.innowise.songmanager.songapi.client.FileApiClient;
import com.innowise.songmanager.songapi.repository.AlbumRepository;
import com.innowise.songmanager.songapi.repository.ArtistRepository;
import com.innowise.songmanager.songapi.repository.SongMetadataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultSongMetadataService implements SongMetadataService {

    private final FileApiClient fileApiClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    private final SongMetadataRepository songMetadataRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    @Override
    @Transactional
    public SongMetadata create(SongMetadata songMetadata) {
        albumRepository.saveIfNotExists(songMetadata.getAlbum());
        artistRepository.saveAllIfNotExists(songMetadata.getArtists());
        songMetadata = songMetadataRepository.save(songMetadata);

        log.info("Successfully created song metadata with id {}", songMetadata.getId());
        return songMetadata;
    }

    @Override
    public List<SongMetadata> getAll() {
        List<SongMetadata> songMetadataList = songMetadataRepository.findAll();
        log.info("Successfully found song metadata list");
        return songMetadataList;
    }

    @Override
    public SongMetadata get(String id) {
        SongMetadata songMetadata = getById(id);
        log.info("Successfully found song metadata with id {}", songMetadata.getId());
        return songMetadata;
    }

    @Override
    @Transactional
    public void delete(String id, String token) {
        SongMetadata songMetadata = getById(id);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("delete-song-file");
        circuitBreaker.run(() -> fileApiClient.deleteSongFile(songMetadata.getId(), token));

        songMetadataRepository.delete(songMetadata);
        log.info("Successfully deleted song metadata with id {}", id);
    }

    private SongMetadata getById(String id) {
        return songMetadataRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(String.format("Can't find song with id %s", id)));
    }
}
