package com.innowise.fileapi.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.exception.ParseException;
import com.innowise.fileapi.service.SqsService;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class DefaultSqsService implements SqsService {

    private final ObjectMapper objectMapper;
    private final SqsTemplate sqsTemplate;

    private static final String QUEUE_NAME = "newSong";

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
