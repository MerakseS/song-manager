package com.innowise.fileapi.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.fileapi.entity.Song;
import com.innowise.fileapi.entity.StorageType;
import com.innowise.fileapi.exception.EntityNotFoundException;
import com.innowise.fileapi.repository.SongRepository;
import com.innowise.fileapi.storage.Storage;
import com.innowise.fileapi.storage.impl.AwsS3Storage;
import com.innowise.fileapi.storage.impl.LocalStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultFileService implements FileService {

    private final SongRepository songRepository;

    private final LocalStorage localStorage;
    private final AwsS3Storage awsS3Storage;

    @Override
    @Transactional
    public Song save(MultipartFile file) {
        Song song = new Song();
        song.setFileName(file.getOriginalFilename());
        song.setFilePath(UUID.randomUUID().toString());

        try {
            awsS3Storage.upload(song.getFilePath(), file);
            song.setStorageType(StorageType.S3);
        }
        catch (Exception e) {
            log.warn("Can't save to s3 storage. Storing to a local storage", e);
            localStorage.upload(song.getFilePath(), file);
            song.setStorageType(StorageType.LOCAL);
        }

        songRepository.save(song);
        log.info("Successfully saved file {} to {} storage", song.getFileName(), song.getStorageType());

        return song;
    }

    @Override
    public Song get(String id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(String.format("Can't find song with id %s", id)));

        Storage storage = getStorageByType(song.getStorageType());
        song.setFile(storage.download(song.getFilePath()));

        log.info("Successfully found song with id {}", id);
        return song;
    }

    private Storage getStorageByType(StorageType storageType) {
        return storageType.equals(StorageType.S3) ? awsS3Storage : localStorage;
    }
}
