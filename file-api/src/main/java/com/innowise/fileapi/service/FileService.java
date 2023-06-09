package com.innowise.fileapi.service;

import org.springframework.web.multipart.MultipartFile;

import com.innowise.contractapi.entity.SongFile;

public interface FileService {

    SongFile save(MultipartFile file);

    SongFile get(String fileId);

    void delete(String fileId);
}
