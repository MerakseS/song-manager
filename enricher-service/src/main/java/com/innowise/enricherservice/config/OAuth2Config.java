package com.innowise.enricherservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OAuth2Config {

    private static final String REGISTRATION_ID = "spotify-client-credentials";

    @Bean
    WebClient spotifyWebClient(ClientRegistrationRepository clientRegistrationRepository) {

        ReactiveClientRegistrationRepository clientRegistrations =
            new InMemoryReactiveClientRegistrationRepository(
                clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID));

        ReactiveOAuth2AuthorizedClientService clientService =
            new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);

        ReactiveOAuth2AuthorizedClientManager authorizedClientManager =
            new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrations, clientService);

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
            new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);

        oauth.setDefaultClientRegistrationId(REGISTRATION_ID);

        return WebClient.builder()
            .baseUrl("https://api.spotify.com/v1")
            .filter(oauth)
            .build();
    }

}
