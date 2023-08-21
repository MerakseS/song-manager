package com.innowise.songmanager.fileapi.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.songmanager.contractapi.entity.SongFile;
import com.innowise.songmanager.contractapi.entity.StorageType;
import com.innowise.songmanager.contractapi.exception.impl.EntityNotFoundException;
import com.innowise.songmanager.contractapi.exception.impl.ParseException;
import com.innowise.songmanager.contractapi.exception.impl.StorageException;
import com.innowise.songmanager.fileapi.repository.SongFileRepository;
import com.innowise.songmanager.fileapi.service.impl.DefaultFileService;
import com.innowise.songmanager.fileapi.storage.impl.AwsS3Storage;
import com.innowise.songmanager.fileapi.storage.impl.LocalStorage;

class DefaultFileServiceTest {

    private String filePath;

    private FileService fileService;
    private SongFileRepository songFileRepository;
    private LocalStorage localStorage;
    private AwsS3Storage s3Storage;

    @BeforeEach
    void setUp() {
        filePath = System.getenv("FILE_PATH");

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
    void save() throws IOException {
        File file = new File(filePath);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
            null, Files.readAllBytes(file.toPath()));
        mockSaveSongFile();

        SongFile songFile = fileService.save(multipartFile);

        Assertions.assertNotNull(songFile.getId());
        Assertions.assertEquals(file.getName(), songFile.getFileName());
        Assertions.assertNotNull(songFile.getFilePath());
        Assertions.assertEquals(StorageType.S3, songFile.getStorageType());
    }

    @Test
    void saveWithoutS3() throws IOException {
        File file = new File(filePath);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
            null, Files.readAllBytes(file.toPath()));

        mockSaveSongFile();
        Mockito.doThrow(StorageException.class).when(s3Storage)
            .upload(Mockito.anyString(), Mockito.eq(multipartFile));

        SongFile songFile = fileService.save(multipartFile);

        Assertions.assertNotNull(songFile.getId());
        Assertions.assertEquals(file.getName(), songFile.getFileName());
        Assertions.assertNotNull(songFile.getFilePath());
        Assertions.assertEquals(StorageType.LOCAL, songFile.getStorageType());
    }

    @Test
    void saveNoMp3() {
        MultipartFile multipartFile = new MockMultipartFile("file", "audio.wav", null, new byte[0]);
        Assertions.assertThrows(ParseException.class, () -> fileService.save(multipartFile));
    }

    @Test
    void get() {
        String id = "1";
        SongFile songFile = getDefaultSongFile(id);
        Mockito.when(songFileRepository.findById(id))
            .thenReturn(Optional.of(songFile));

        SongFile actualSongFile = fileService.get(id);

        Assertions.assertEquals(id, actualSongFile.getId());
    }

    @Test
    void getUnknown() {
        String id = "UnknownId";
        Mockito.when(songFileRepository.findById(id))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> fileService.get(id));
    }

    @Test
    void delete() {
        String id = "1";
        SongFile songFile = getDefaultSongFile(id);
        Mockito.when(songFileRepository.findById(id))
            .thenReturn(Optional.of(songFile));

        fileService.delete(id);

        Mockito.verify(localStorage).delete(songFile.getFilePath());
        Mockito.verify(songFileRepository).delete(songFile);
    }

    @Test
    void deleteUnknown() {
        String id = "UnknownId";
        Mockito.when(songFileRepository.findById(id))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
            () -> fileService.delete(id));
    }

    private void mockSaveSongFile() {
        Mockito.when(songFileRepository.save(Mockito.any(SongFile.class)))
            .thenAnswer(invocation -> {
                SongFile songFile = invocation.getArgument(0, SongFile.class);
                songFile.setId("1");
                return songFile;
            });
    }

    private SongFile getDefaultSongFile(String id) {
        SongFile songFile = new SongFile();
        songFile.setId(id);
        songFile.setStorageType(StorageType.LOCAL);
        songFile.setFilePath(filePath);

        return songFile;
    }
}