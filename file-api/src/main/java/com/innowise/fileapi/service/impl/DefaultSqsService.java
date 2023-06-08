package com.innowise.fileapi.service.impl;

import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.exception.ParseException;
import com.innowise.fileapi.service.SqsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultSqsService implements SqsService {

    private final ObjectMapper objectMapper;
    private final ProducerTemplate producerTemplate;

    private static final String URI_FORMAT = "aws2-sqs://%s?amazonSQSClient=#client&autoCreateQueue=true";
    private static final String QUEUE_NAME = "newSong";

    @Override
    public void sendNewSong(SongTagsDto songTagsDto) {
        try {
            producerTemplate.sendBody(String.format(URI_FORMAT, QUEUE_NAME),
                objectMapper.writeValueAsString(songTagsDto));
            log.info("Sent {} in queue {}", songTagsDto, QUEUE_NAME);
        }
        catch (JsonProcessingException e) {
            throw new ParseException(e);
        }
    }
}
