package com.innowise.fileapi.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.innowise.contractapi.entity.SongFile;
import com.innowise.fileapi.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final static String DISPOSITION_FORMAT = "attachment; filename=\"%s\"";

    @PostMapping
    public ResponseEntity<Object> uploadFile(@RequestPart("file") MultipartFile file) {
        SongFile songFile = fileService.save(file);
        return ResponseEntity.ok().body(songFile);
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
    public ResponseEntity<Object> deleteFile(@PathVariable("fileId") String fileId) {
        fileService.delete(fileId);
        return ResponseEntity.ok().build();
    }
}
