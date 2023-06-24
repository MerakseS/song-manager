package com.innowise.enricherservice.route;

import java.util.Objects;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.entity.SongMetadata;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SqsRouteBuilder extends RouteBuilder {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private static final String SQS_URI_FORMAT = "aws2-sqs://%s?amazonSQSClient=#client&autoCreateQueue=true";
    private static final String CONSUMER_QUEUE_NAME = "songTags";
    private static final String PRODUCER_QUEUE_NAME = "songMetadata";

    private static final String REGISTRATION_ID = "spotify-client-credentials";

    @Value("${spring.security.oauth2.client.registration.spotify-client-credentials.client-secret}")
    private String clientSecret;

    @Override
    public void configure() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
            .withClientRegistrationId(REGISTRATION_ID)
            .principal(clientSecret)
            .build();

        from(String.format(SQS_URI_FORMAT, CONSUMER_QUEUE_NAME))
            .convertBodyTo(SongTagsDto.class)
            .setProperty("songId", simple("${body.songId}"))
            .setHeader("Authorization", constant(getBearer(authorizeRequest)))
            .setHeader(Exchange.HTTP_QUERY, simple(
                "q=remaster%2520track:${body.title}%2520artist:${body.artist}&type=track&limit=1"))
            .to("https://api.spotify.com/v1/search")
            .convertBodyTo(SongMetadata.class)
            .marshal().json()
            .log("Successfully found song metadata: ${body}")
            .to(String.format(SQS_URI_FORMAT, PRODUCER_QUEUE_NAME));
    }

    private String getBearer(OAuth2AuthorizeRequest authorizeRequest) {
        String token = Objects.requireNonNull(authorizedClientManager.authorize(authorizeRequest))
            .getAccessToken()
            .getTokenValue();

        return String.format("Bearer %s", token);
    }
}
