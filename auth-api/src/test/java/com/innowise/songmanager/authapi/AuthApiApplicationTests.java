package com.innowise.songmanager.authapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthApiApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void login() {
        given()
            .header("Accept", "text/html")
            .queryParam("response_type", "code")
            .queryParam("client_id", "songs-client")
            .queryParam("redirect_uri", "http://localhost:4200/authorized")
            .when().get("/oauth2/authorize")
            .then().statusCode(HttpStatus.OK.value())
            .extract().response();

//        given()
//            .log().everything()
//            .contentType(ContentType.URLENC)
//            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//            .cookie("JSESSIONID", cookie)
//            .formParam("username", "user")
//            .formParam("password", "1234")
//            .when().post("/login")
//            .then().log().everything();
    }
}
