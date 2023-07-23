package com.innowise.songmanager.fileapi.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.songmanager.contractapi.dto.SongTagsDto;
import com.innowise.songmanager.contractapi.exception.ParseException;
import com.innowise.songmanager.fileapi.service.SqsService;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultSqsService implements SqsService {

    private static final String QUEUE_NAME = "songTags";

    private final ObjectMapper objectMapper;
    private final SqsTemplate sqsTemplate;

    @Override
    public void sendNewSong(SongTagsDto songTagsDto) {
        try {
            sqsTemplate.send(QUEUE_NAME, objectMapper.writeValueAsString(songTagsDto));
        }
        catch (JsonProcessingException e) {
            throw new ParseException(e);
        }
    }
}
