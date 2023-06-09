package com.innowise.songapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.entity.SongMetadata;
import com.innowise.songapi.mapper.SongMetadataMapper;
import com.innowise.songapi.service.SongMetadataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongMetadataMapper songMetadataMapper;
    private final SongMetadataService songMetadataService;

    @GetMapping
    public ResponseEntity<List<SongMetadataDto>> getAll() {
        List<SongMetadata> songMetadataList = songMetadataService.getAll();
        List<SongMetadataDto> songMetadataDtoList = songMetadataMapper.mapEntityListToDtoList(songMetadataList);
        return ResponseEntity.ok(songMetadataDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongMetadataDto> get(@PathVariable("id") String id) {
        SongMetadata songMetadata = songMetadataService.get(id);
        SongMetadataDto songMetadataDto = songMetadataMapper.mapEntityToDto(songMetadata);
        return ResponseEntity.ok(songMetadataDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        songMetadataService.delete(id);
        return ResponseEntity.ok().build();
    }
}