package com.innowise.enricherservice.service;

import com.innowise.contractapi.dto.SongMetadataDto;

import io.awspring.cloud.sqs.annotation.SqsListener;

public interface SqsService {

    @SqsListener("newSong")
    void consumeNewSong(String songTagsJson);

    void sendSongMetadata(SongMetadataDto songMetadataDto);
}
