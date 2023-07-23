package com.innowise.songmanager.songapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innowise.songmanager.contractapi.entity.Album;
import com.innowise.songmanager.contractapi.entity.Artist;
import com.innowise.songmanager.contractapi.entity.SongMetadata;
import com.innowise.songmanager.contractapi.exception.EntityNotFoundException;
import com.innowise.songmanager.songapi.client.FileApiClient;
import com.innowise.songmanager.songapi.repository.AlbumRepository;
import com.innowise.songmanager.songapi.repository.ArtistRepository;
import com.innowise.songmanager.songapi.repository.SongMetadataRepository;
import com.innowise.songmanager.songapi.service.SongMetadataService;

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
    public void create(SongMetadata songMetadata) {
        saveAlbum(songMetadata.getAlbum());
        saveArtists(songMetadata.getArtists());
        songMetadataRepository.save(songMetadata);
        log.info("Successfully created song metadata with id {}", songMetadata.getId());
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

        songMetadataRepository.deleteById(songMetadata.getId());
        log.info("Successfully deleted song metadata with id {}", id);
    }

    private SongMetadata getById(String id) {
        return songMetadataRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(String.format("Can't find song with id %s", id)));
    }

    private void saveAlbum(Album album) {
        Optional<Album> optionalAlbum = albumRepository.findAlbumByName(album.getName());
        if (optionalAlbum.isEmpty()) {
            albumRepository.save(album);
        }
        else {
            album.setId(optionalAlbum.get().getId());
        }
    }

    private void saveArtists(List<Artist> artistList) {
        for (Artist artist : artistList) {
            Optional<Artist> optionalArtist = artistRepository.findArtistByName(artist.getName());
            if (optionalArtist.isEmpty()) {
                artistRepository.save(artist);
            }
            else {
                artist.setId(optionalArtist.get().getId());
            }
        }
    }
}
