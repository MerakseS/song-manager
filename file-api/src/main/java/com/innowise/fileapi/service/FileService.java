package com.innowise.fileapi.service;

import org.springframework.web.multipart.MultipartFile;

import com.innowise.fileapi.entity.Song;

public interface FileService {

    Song save(MultipartFile file);

    Song get(String fileId);
}
