package com.innowise.enricherservice.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.exception.ParseException;
import com.innowise.enricherservice.service.SpotifyService;
import com.innowise.enricherservice.service.SqsService;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultSqsService implements SqsService {

    private final ObjectMapper objectMapper;
    private final SpotifyService spotifyService;
    private final SqsTemplate sqsTemplate;

    private final static String ENDPOINT_NAME = "songMetadata";

    public void consumeNewSong(String songTagsJson) {
        SongTagsDto songTagsDto = parseToObject(songTagsJson);
        SongMetadataDto songMetadataDto = spotifyService.getSongMetadata(songTagsDto);
        sendSongMetadata(songMetadataDto);
    }

    @Override
    public void sendSongMetadata(SongMetadataDto songMetadataDto) {
        sqsTemplate.send(ENDPOINT_NAME, parseToJson(songMetadataDto));
    }

    private SongTagsDto parseToObject(String songDtoStr) {
        try {
            return objectMapper.readValue(songDtoStr, SongTagsDto.class);
        }
        catch (JsonProcessingException e) {
            throw new ParseException(e);
        }
    }

    private String parseToJson(SongMetadataDto songMetadataDto) {
        try {
            return objectMapper.writeValueAsString(songMetadataDto);
        }
        catch (JsonProcessingException e) {
            throw new ParseException(e);
        }
    }
}
