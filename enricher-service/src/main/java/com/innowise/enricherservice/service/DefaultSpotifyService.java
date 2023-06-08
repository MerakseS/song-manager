package com.innowise.enricherservice.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.contractapi.dto.AlbumDto;
import com.innowise.contractapi.dto.ArtistDto;
import com.innowise.contractapi.dto.SongMetadataDto;
import com.innowise.contractapi.dto.SongTagsDto;
import com.innowise.contractapi.exception.ParseException;
import com.innowise.enricherservice.service.SpotifyService;

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
    public SongMetadataDto getSongMetadata(SongTagsDto songTagsDto) {
        String response = getSpotifyData(songTagsDto);
        SongMetadataDto songMetadataDto = parseSongMetadata(response);
        songMetadataDto.setId(songTagsDto.getSongId());

        log.info("Successfully found song metadata: {}", songMetadataDto);
        return songMetadataDto;
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

    private SongMetadataDto parseSongMetadata(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode trackNode = root.at("/tracks/items/0");

            SongMetadataDto songMetadataDto = new SongMetadataDto();
            songMetadataDto.setName(trackNode.get("name").asText());
            songMetadataDto.setLink(trackNode.at("/external_urls/spotify").asText());
            songMetadataDto.setAlbum(parseAlbum(trackNode));
            songMetadataDto.setArtists(parseArtistList(trackNode));

            return songMetadataDto;
        }
        catch (JsonProcessingException e) {
            throw new ParseException(e);
        }
    }

    private static AlbumDto parseAlbum(JsonNode trackNode) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setName(trackNode.at("/album/name").asText());
        albumDto.setLink(trackNode.at("/album/external_urls/spotify").asText());
        return albumDto;
    }

    private static ArrayList<ArtistDto> parseArtistList(JsonNode trackNode) {
        ArrayList<ArtistDto> artistDtoList = new ArrayList<>();
        for (JsonNode artistNode : trackNode.get("artists")) {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setName(artistNode.get("name").asText());
            artistDto.setLink(artistNode.at("/external_urls/spotify").asText());
            artistDtoList.add(artistDto);
        }

        return artistDtoList;
    }
}
