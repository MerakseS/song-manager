package com.innowise.contractapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class SongMetadataDto {

    private String id;

    private String name;

    private String link;

    private long duration;

    private AlbumDto album;

    private List<ArtistDto> artists;
}
