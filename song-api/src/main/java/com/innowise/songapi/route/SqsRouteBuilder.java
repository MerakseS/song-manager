package com.innowise.songapi.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.entity.SongMetadata;
import com.innowise.contractapi.mapper.SongMetadataMapper;
import com.innowise.songapi.service.SongMetadataService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SqsRouteBuilder extends RouteBuilder {

    private final SongMetadataMapper songMetadataMapper;
    private final SongMetadataService songMetadataService;

    private static final String URI_FORMAT = "aws2-sqs://%s?amazonSQSClient=#client&autoCreateQueue=true";
    private static final String QUEUE_NAME = "songMetadata";

    @Override
    public void configure() {
        from(String.format(URI_FORMAT, QUEUE_NAME))
            .process(exchange -> {
                SongMetadataDto songMetadataDto = exchange.getIn().getBody(SongMetadataDto.class);
                SongMetadata songMetadata = songMetadataMapper.mapDtoToEntity(songMetadataDto);
                songMetadataService.create(songMetadata);
            });
    }
}
