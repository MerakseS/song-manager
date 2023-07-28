package com.innowise.songmanager.songapi;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;

import com.innowise.songmanager.contractapi.entity.Album;
import com.innowise.songmanager.contractapi.entity.Artist;
import com.innowise.songmanager.contractapi.entity.SongMetadata;
import com.innowise.songmanager.songapi.service.SongMetadataService;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SongApiApplicationTests {

    private static final String UNKNOWN_ID = "000000000000000000000000";

    @LocalServerPort
    private int port;

    @Value("${test.token.user}")
    private String userToken;

    @Value("${test.token.admin}")
    private String adminToken;

    @Autowired
    private SongMetadataService songMetadataService;

    private SongMetadata songMetadata;

    @BeforeAll
    void setUp() {
        RestAssured.port = port;
        songMetadata = createTestData();
    }

    @Test
    void getAllUnauthorized() {
        given()
            .when().get("/songs")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void getAll() {
        given().contentType(ContentType.JSON)
            .header("Authorization", String.format("Bearer %s", userToken))

            .when().get("/songs")

            .then().statusCode(HttpStatus.OK.value())
            .body("items", isA(List.class));
    }

    @Test
    void getUnauthorized() {
        given()
            .when().get("/songs/{id}", songMetadata.getId())
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void getUnknown() {
        given().header("Authorization", String.format("Bearer %s", userToken))
            .when().get("/songs/{id}", UNKNOWN_ID)
            .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void get() {
        given().contentType(ContentType.JSON)
            .header("Authorization", String.format("Bearer %s", userToken))

            .when().get("/songs/{id}", songMetadata.getId())

            .then().statusCode(HttpStatus.OK.value())
            .body("name", equalTo("testSong"));
    }

    @Test
    void deleteUnauthorized() {
        given()
            .when().delete("/songs/{id}", songMetadata.getId())
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void deleteForbidden() {
        given().header("Authorization", String.format("Bearer %s", userToken))
            .when().delete("/songs/{id}", songMetadata.getId())
            .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void deleteUnknown() {
        given().header("Authorization", String.format("Bearer %s", adminToken))
            .when().delete("/songs/{id}", UNKNOWN_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private SongMetadata createTestData() {
        SongMetadata songMetadata = songMetadataBuilder().id(null).build();
        return songMetadataService.create(songMetadata);
    }

    private SongMetadata.SongMetadataBuilder songMetadataBuilder() {
        Album album = new Album();
        album.setName("album");
        album.setLink("albumLink");

        Artist artist = new Artist();
        artist.setName("artist");
        artist.setLink("artistLink");

        return SongMetadata.builder()
            .id("1")
            .name("testSong")
            .link("songLink")
            .duration(1L)
            .album(album)
            .artists(List.of(artist));
    }
}
