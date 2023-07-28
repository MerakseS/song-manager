package com.innowise.songmanager.fileapi;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileApiApplicationTests {

    private static final String UNKNOWN_ID = "000000000000000000000000";

    @LocalServerPort
    private int port;

    @Value("${test.token.user}")
    private String userToken;

    @Value("${test.token.admin}")
    private String adminToken;

    @Value("${test.file-path}")
    private String filePath;

    private String fileId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void uploadUnauthorized() {
        File file = new File(filePath);

        given().multiPart("file", file)
            .when().post("/files")
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void uploadForbidden() {
        File file = new File(filePath);

        given().header("Authorization", String.format("Bearer %s", userToken))
            .multiPart("file", file)
            .when().post("/files")
            .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @Order(1)
    void upload() {
        File file = new File(filePath);

        fileId = given().header("Authorization", String.format("Bearer %s", adminToken))
            .multiPart("file", file)

            .when().post("/files")

            .then().statusCode(HttpStatus.CREATED.value())
            .body("fileName", equalTo(file.getName()))
            .extract().jsonPath().getString("id");
    }

    @Test
    void downloadUnauthorized() {
        given()
            .when().get("/files/{id}", fileId)
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void downloadUnknown() {
        given().header("Authorization", String.format("Bearer %s", userToken))
            .when().get("/files/{id}", UNKNOWN_ID)
            .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(2)
    void download() {
        given().header("Authorization", String.format("Bearer %s", userToken))
            .when().get("/files/{id}", fileId)
            .then().statusCode(HttpStatus.OK.value())
            .body(notNullValue());
    }

    @Test
    void deleteUnauthorized() {
        given()
            .when().delete("/files/{id}", fileId)
            .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void deleteForbidden() {
        given().header("Authorization", String.format("Bearer %s", userToken))
            .when().delete("/files/{id}", fileId)
            .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void deleteUnknown() {
        given().header("Authorization", String.format("Bearer %s", adminToken))
            .when().delete("/files/{id}", UNKNOWN_ID)
            .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void delete() {
        given().header("Authorization", String.format("Bearer %s", adminToken))
            .when().delete("/files/{id}", fileId)
            .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

}
