package com.innowise.songmanager.fileapi.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.songmanager.contractapi.dto.SongTagsDto;
import com.innowise.songmanager.contractapi.entity.SongFile;
import com.innowise.songmanager.contractapi.entity.StorageType;
import com.innowise.songmanager.contractapi.exception.impl.EntityNotFoundException;
import com.innowise.songmanager.contractapi.exception.impl.ParseException;
import com.innowise.songmanager.fileapi.repository.SongFileRepository;
import com.innowise.songmanager.fileapi.service.FileService;
import com.innowise.songmanager.fileapi.service.SqsService;
import com.innowise.songmanager.fileapi.storage.Storage;
import com.innowise.songmanager.fileapi.storage.impl.AwsS3Storage;
import com.innowise.songmanager.fileapi.storage.impl.LocalStorage;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultFileService implements FileService {

    private final SqsService sqsService;

    private final LocalStorage localStorage;
    private final AwsS3Storage awsS3Storage;

    private final SongFileRepository songFileRepository;

    @Override
    @Transactional
    public SongFile save(MultipartFile file) {
        SongFile songFile = new SongFile();
        songFile.setFileName(file.getOriginalFilename());
        songFile.setFilePath(UUID.randomUUID().toString());

        storeFile(file, songFile);
        songFileRepository.save(songFile);
        log.info("Successfully saved file {} to {} storage", songFile.getFileName(), songFile.getStorageType());

        SongTagsDto songTagsDto = parseMp3(file);
        songTagsDto.setSongId(songFile.getId());
        sqsService.sendNewSong(songTagsDto);
        log.info("Successfully sent tags of song with id {}", songTagsDto.getSongId());

        return songFile;
    }

    @Override
    public SongFile get(String id) {
        SongFile songFile = getById(id);
        Storage storage = getStorageByType(songFile.getStorageType());
        Resource file = storage.download(songFile.getFilePath());
        songFile.setFile(file);

        log.info("Successfully found song with id {}", id);
        return songFile;
    }

    @Override
    @Transactional
    public void delete(String fileId) {
        SongFile songFile = getById(fileId);
        Storage storage = getStorageByType(songFile.getStorageType());
        storage.delete(songFile.getFilePath());
        songFileRepository.delete(songFile);

        log.info("Successfully deleted song with id {}", fileId);
    }

    private void storeFile(MultipartFile file, SongFile songFile) {
        try {
            awsS3Storage.upload(songFile.getFilePath(), file);
            songFile.setStorageType(StorageType.S3);
        }
        catch (Exception e) {
            log.warn("Can't save to s3 storage", e);
            localStorage.upload(songFile.getFilePath(), file);
            songFile.setStorageType(StorageType.LOCAL);
        }
    }

    private SongFile getById(String id) {
        return songFileRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(String.format("Can't find song with id %s", id)));
    }

    private Storage getStorageByType(StorageType storageType) {
        return storageType.equals(StorageType.S3) ? awsS3Storage : localStorage;
    }

    private static SongTagsDto parseMp3(MultipartFile multipartFile) {
        try {
            Path tempFile = Files.createTempFile(null, ".mp3");
            multipartFile.transferTo(tempFile.toFile());
            Mp3File mp3File = new Mp3File(tempFile);
            ID3v1 id3v1Tag = mp3File.getId3v1Tag();

            SongTagsDto songTagsDto = new SongTagsDto();
            songTagsDto.setTitle(id3v1Tag.getTitle());
            songTagsDto.setArtist(id3v1Tag.getArtist());

            return songTagsDto;
        }
        catch (Exception e) {
            throw new ParseException("Can't parse mp3 tags", e);
        }
    }
}
