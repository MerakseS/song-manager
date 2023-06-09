package com.innowise.enricherservice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.entity.SongMetadata;
import com.innowise.contractapi.mapper.SongMetadataMapper;
import com.innowise.enricherservice.service.SpotifyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsRouteBuilder extends RouteBuilder {

    private final SpotifyService spotifyService;
    private final SongMetadataMapper songMetadataMapper;
    private final ObjectMapper objectMapper;

    private static final String URI_FORMAT = "aws2-sqs://%s?amazonSQSClient=#client&autoCreateQueue=true";
    private static final String CONSUMER_QUEUE_NAME = "newSong";
    private static final String PRODUCER_QUEUE_NAME = "songMetadata";

    @Override
    public void configure() {
        from(String.format(URI_FORMAT, CONSUMER_QUEUE_NAME))
            .process(exchange -> {
                SongTagsDto songTagsDto = exchange.getIn().getBody(SongTagsDto.class);
                SongMetadata songMetadata = spotifyService.getSongMetadata(songTagsDto);
                SongMetadataDto songMetadataDto = songMetadataMapper.mapEntityToDto(songMetadata);
                exchange.getIn().setBody(objectMapper.writeValueAsString(songMetadataDto));
            })
            .to(String.format(URI_FORMAT, PRODUCER_QUEUE_NAME));
    }
}
