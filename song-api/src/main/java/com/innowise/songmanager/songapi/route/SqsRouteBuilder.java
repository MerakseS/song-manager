package com.innowise.songmanager.songapi.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.innowise.songmanager.contractapi.entity.SongMetadata;
import com.innowise.songmanager.songapi.service.SongMetadataService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SqsRouteBuilder extends RouteBuilder {

    private final SongMetadataService songMetadataService;

    private static final String URI_FORMAT = "aws2-sqs://%s?amazonSQSClient=#client&autoCreateQueue=true";
    private static final String QUEUE_NAME = "songMetadata";

    @Override
    public void configure() {
        from(String.format(URI_FORMAT, QUEUE_NAME))
            .convertBodyTo(SongMetadata.class)
            .process(exchange -> {
                SongMetadata songMetadata = exchange.getIn().getBody(SongMetadata.class);
                songMetadataService.create(songMetadata);
            });
    }
}
