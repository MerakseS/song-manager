package com.innowise.songmanager.songapi.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import com.innowise.songmanager.contractapi.entity.Album;
import com.innowise.songmanager.contractapi.entity.Artist;
import com.innowise.songmanager.contractapi.entity.SongMetadata;
import com.innowise.songmanager.contractapi.entity.SongMetadata.SongMetadataBuilder;
import com.innowise.songmanager.contractapi.exception.impl.EntityNotFoundException;
import com.innowise.songmanager.songapi.client.FileApiClient;
import com.innowise.songmanager.songapi.repository.AlbumRepository;
import com.innowise.songmanager.songapi.repository.ArtistRepository;
import com.innowise.songmanager.songapi.repository.SongMetadataRepository;

class DefaultSongMetadataServiceTest {

    private SongMetadataRepository songMetadataRepository;
    private SongMetadataService songMetadataService;
    private CircuitBreakerFactory circuitBreakerFactory;

    @BeforeEach
    void setUp() {
        songMetadataRepository = Mockito.mock(SongMetadataRepository.class);
        circuitBreakerFactory = Mockito.mock(CircuitBreakerFactory.class);
        songMetadataService = new DefaultSongMetadataService(
            Mockito.mock(FileApiClient.class),
            circuitBreakerFactory,
            songMetadataRepository,
            Mockito.mock(AlbumRepository.class),
            Mockito.mock(ArtistRepository.class)
        );
    }

    @Test
    void create() {
        SongMetadata songMetadata = songMetadataBuilder().id(null).build();
        SongMetadata createdSongMetadata = songMetadataBuilder().build();
        Mockito.when(songMetadataRepository.save(songMetadata))
            .thenReturn(createdSongMetadata);

        SongMetadata actualSongMetadata = songMetadataService.create(songMetadata);

        Mockito.verify(songMetadataRepository).save(songMetadata);
        SongMetadata expectedSongMetadata = songMetadataBuilder().build();
        Assertions.assertEquals(expectedSongMetadata, actualSongMetadata);
    }

    @Test
    void getAll() {
        List<SongMetadata> actualList = songMetadataService.getAll();

        Mockito.verify(songMetadataRepository).findAll();
        Assertions.assertNotNull(actualList);
    }

    @Test
    void get() {
        String id = "1";
        Mockito.when(songMetadataRepository.findById(id))
            .thenReturn(Optional.of(songMetadataBuilder().build()));

        SongMetadata songMetadata = songMetadataService.get(id);

        Assertions.assertEquals(id, songMetadata.getId());
    }

    @Test
    void getUnknown() {
        String id = "unknownId";
        Mockito.when(songMetadataRepository.findById(id))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> songMetadataService.get(id));
    }

    @Test
    void delete() {
        String id = "1";
        String token = "token";
        SongMetadata songMetadata = songMetadataBuilder().build();
        Mockito.when(songMetadataRepository.findById(id))
            .thenReturn(Optional.of(songMetadata));
        Mockito.when(circuitBreakerFactory.create("delete-song-file"))
            .thenReturn(Mockito.mock(CircuitBreaker.class));

        songMetadataService.delete(id, token);

        Mockito.verify(songMetadataRepository).delete(songMetadata);
    }

    @Test
    void deleteUnknown() {
        String id = "unknownId";
        String token = "token";
        Mockito.when(songMetadataRepository.findById(id))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> songMetadataService.delete(id, token));
    }

    private SongMetadataBuilder songMetadataBuilder() {
        Album album = new Album();
        album.setName("album");
        album.setLink("albumLink");

        Artist artist = new Artist();
        artist.setName("artist");
        artist.setLink("artistLink");

        return SongMetadata.builder()
            .id("1")
            .name("song")
            .link("songLink")
            .duration(1L)
            .album(album)
            .artists(List.of(artist));
    }
}