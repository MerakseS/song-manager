package com.innowise.songmanager.fileapi.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.songmanager.contractapi.entity.SongFile;
import com.innowise.songmanager.fileapi.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final static String DISPOSITION_FORMAT = "attachment; filename=\"%s\"";

    private final FileService fileService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> uploadFile(@RequestPart("file") MultipartFile file) {
        SongFile songFile = fileService.save(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(songFile);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) {
        SongFile songFile = fileService.get(fileId);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                String.format(DISPOSITION_FORMAT, songFile.getFileName()))
            .body(songFile.getFile());
    }

    @DeleteMapping("/{fileId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteFile(@PathVariable("fileId") String fileId) {
        fileService.delete(fileId);
        return ResponseEntity.noContent().build();
    }
}
