package com.innowise.songmanager.fileapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import com.innowise.songmanager.fileapi.repository.SongFileRepository;
import com.innowise.songmanager.fileapi.service.impl.DefaultFileService;
import com.innowise.songmanager.fileapi.storage.Storage;
import com.innowise.songmanager.fileapi.storage.impl.AwsS3Storage;
import com.innowise.songmanager.fileapi.storage.impl.LocalStorage;

class DefaultFileServiceTest {

    private FileService fileService;
    private SongFileRepository songFileRepository;
    private LocalStorage localStorage;
    private AwsS3Storage s3Storage;

    @BeforeEach
    void setUp() {
        localStorage = Mockito.mock(LocalStorage.class);
        s3Storage = Mockito.mock(AwsS3Storage.class);
        songFileRepository = Mockito.mock(SongFileRepository.class);
        fileService = new DefaultFileService(
            Mockito.mock(SqsService.class),
            localStorage,
            s3Storage,
            songFileRepository
        );
    }

    @Test
    void save() {

    }

    @Test
    void get() {
    }

    @Test
    void delete() {
    }
}