package com.innowise.songmanager.enricherservice.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.TypeConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.songmanager.contractapi.entity.Album;
import com.innowise.songmanager.contractapi.entity.Artist;
import com.innowise.songmanager.contractapi.entity.SongMetadata;
import com.innowise.songmanager.contractapi.exception.impl.ParseException;

import lombok.RequiredArgsConstructor;

@Converter(generateLoader = true)
@Component
@RequiredArgsConstructor
public class SpotifyJsonConverter implements TypeConverter {

    private final ObjectMapper objectMapper;

    @Override
    public boolean allowNull() {
        return false;
    }

    @Override
    public <T> T convertTo(Class<T> type, Object value) throws TypeConversionException {
        try {
            JsonNode root = objectMapper.readTree((InputStream) value);
            JsonNode trackNode = root.at("/tracks/items/0");

            SongMetadata songMetadata = new SongMetadata();
            songMetadata.setName(trackNode.get("name").asText());
            songMetadata.setLink(trackNode.at("/external_urls/spotify").asText());
            songMetadata.setDuration(trackNode.get("duration_ms").asLong());
            songMetadata.setAlbum(parseAlbum(trackNode));
            songMetadata.setArtists(parseArtistList(trackNode));

            return (T) songMetadata;
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        SongMetadata songMetadata = (SongMetadata) convertTo(type, value);
        songMetadata.setId((String) exchange.getProperty("songId"));
        return (T) songMetadata;
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

    @Override
    public <T> T mandatoryConvertTo(Class<T> type, Object value) throws TypeConversionException {
        return convertTo(type, value);
    }

    @Override
    public <T> T mandatoryConvertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        return convertTo(type, exchange, value);
    }

    @Override
    public <T> T tryConvertTo(Class<T> type, Object value) {
        return convertTo(type, value);
    }

    @Override
    public <T> T tryConvertTo(Class<T> type, Exchange exchange, Object value) {
        return convertTo(type, exchange, value);
    }
}
