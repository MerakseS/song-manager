package com.innowise.songmanager.enricherservice.route;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import com.innowise.songmanager.contractapi.dto.SongTagsDto;
import com.innowise.songmanager.contractapi.entity.SongMetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsRouteBuilder extends RouteBuilder {

    private static final String SQS_URI_FORMAT = "aws2-sqs://%s?amazonSQSClient=#client&autoCreateQueue=true";
    private static final String CONSUMER_QUEUE_NAME = "songTags";
    private static final String PRODUCER_QUEUE_NAME = "songMetadata";

    private static final String REGISTRATION_ID = "spotify-client-credentials";

    @Value("${spring.security.oauth2.client.registration.spotify-client-credentials.client-secret}")
    private String clientSecret;

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private String spotifyToken;

    @Override
    public void configure() {
        from(String.format(SQS_URI_FORMAT, CONSUMER_QUEUE_NAME))
            .convertBodyTo(SongTagsDto.class)
            .setProperty("songId", simple("${body.songId}"))
            .setHeader("Authorization", () -> spotifyToken)
            .setHeader(Exchange.HTTP_QUERY, simple(
                "q=remaster%2520track:${body.title}%2520artist:${body.artist}&type=track&limit=1"))
            .to("https://api.spotify.com/v1/search")
            .convertBodyTo(SongMetadata.class)
            .marshal().json()
            .log("Successfully found song metadata: ${body}")
            .to(String.format(SQS_URI_FORMAT, PRODUCER_QUEUE_NAME));
    }

    @Scheduled(fixedRate = 3600, timeUnit = TimeUnit.SECONDS)
    private void updateSpotifyToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
            .withClientRegistrationId(REGISTRATION_ID)
            .principal(clientSecret)
            .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager
            .authorize(authorizeRequest);

        String token = Objects.requireNonNull(authorizedClient)
            .getAccessToken()
            .getTokenValue();

        spotifyToken = String.format("Bearer %s", token);
        log.info("Successfully updated spotify token {}", token);
    }
}
