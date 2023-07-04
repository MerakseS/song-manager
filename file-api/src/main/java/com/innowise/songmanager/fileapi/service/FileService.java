package com.innowise.songmanager.fileapi.service;

import org.springframework.web.multipart.MultipartFile;

import com.innowise.songmanager.contractapi.entity.SongFile;

public interface FileService {

    SongFile save(MultipartFile file);

    SongFile get(String fileId);

    void delete(String fileId);
}
