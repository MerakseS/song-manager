package com.innowise.enricherservice.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.entity.Album;
import com.innowise.contractapi.entity.Artist;
import com.innowise.contractapi.entity.SongMetadata;
import com.innowise.contractapi.exception.ParseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultSpotifyService implements SpotifyService {

    private final ObjectMapper objectMapper;

    private final WebClient apiClient = WebClient.create("https://api.spotify.com/v1");

    @Value("${enricher-service.spotify-token}")
    private String spotifyToken;

    @Override
    public SongMetadata getSongMetadata(SongTagsDto songTagsDto) {
        String response = getSpotifyData(songTagsDto);
        SongMetadata songMetadata = parseSongMetadata(response);
        songMetadata.setId(songTagsDto.getSongId());

        log.info("Successfully found song metadata: {}", songMetadata);
        return songMetadata;
    }

    private String getSpotifyData(SongTagsDto songTagsDto) {
        return apiClient.get()
            .uri(uriBuilder -> uriBuilder.path("/search")
                .queryParam("q", String.format("remaster%%20track:%s%%20artist:%s",
                    songTagsDto.getTitle(), songTagsDto.getArtist()))
                .queryParam("type", "track")
                .queryParam("limit", 1)
                .build())
            .headers(httpHeaders -> httpHeaders.setBearerAuth(spotifyToken))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    private SongMetadata parseSongMetadata(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode trackNode = root.at("/tracks/items/0");

            SongMetadata songMetadata = new SongMetadata();
            songMetadata.setName(trackNode.get("name").asText());
            songMetadata.setLink(trackNode.at("/external_urls/spotify").asText());
            songMetadata.setAlbum(parseAlbum(trackNode));
            songMetadata.setArtists(parseArtistList(trackNode));

            return songMetadata;
        }
        catch (JsonProcessingException e) {
            throw new ParseException(e);
        }
    }

    private static Album parseAlbum(JsonNode trackNode) {
        Album album = new Album();
        album.setName(trackNode.at("/album/name").asText());
        album.setLink(trackNode.at("/album/external_urls/spotify").asText());
        return album;
    }

    private static ArrayList<Artist> parseArtistList(JsonNode trackNode) {
        ArrayList<Artist> artistList = new ArrayList<>();
        for (JsonNode artistNode : trackNode.get("artists")) {
            Artist artist = new Artist();
            artist.setName(artistNode.get("name").asText());
            artist.setLink(artistNode.at("/external_urls/spotify").asText());
            artistList.add(artist);
        }

        return artistList;
    }
}
