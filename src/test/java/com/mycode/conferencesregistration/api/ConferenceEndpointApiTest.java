package com.mycode.conferencesregistration.api;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yurii Kovtun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(value = {"/clear-conference.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ConferenceEndpointApiTest {

    @Autowired
    protected WebApplicationContext context;

    @LocalServerPort
    protected int port;


    @Before
    public void init() {
        RestAssured.port = port;
    }

    @Test
    public void conferenceCouldBeAdded() {
        int id = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("{\n" +
                        "\"name\": \"Name Conference\",\n" +
                        "\"topic\": \"Topic Conference\",\n" +
                        "\"dateStart\": \"2090-01-01\",\n" +
                        "\"amountParticipants\": 100" +
                        "\n}")
                .when()
                    .post("conferences")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .extract().body().jsonPath().get("id");

        assertThat(id).isPositive();
    }
}
