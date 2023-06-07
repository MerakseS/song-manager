package com.innowise.enricherservice.service;

import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.dto.SongTagsDto;

public interface SpotifyService {

    SongMetadataDto getSongMetadata(SongTagsDto songTagsDto);
}
