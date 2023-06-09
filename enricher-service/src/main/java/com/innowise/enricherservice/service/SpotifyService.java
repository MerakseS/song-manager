package com.innowise.enricherservice.service;

import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.entity.SongMetadata;

public interface SpotifyService {

    SongMetadata getSongMetadata(SongTagsDto songTagsDto);
}
