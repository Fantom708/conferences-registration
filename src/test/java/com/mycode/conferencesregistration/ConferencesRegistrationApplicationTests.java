package com.mycode.conferencesregistration;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ConferencesRegistrationApplicationTests {

    @Autowired
    protected WebApplicationContext context;

    @LocalServerPort
    protected int port;


    @Before
    public void init() {
        RestAssured.port = port;
    }

    @Test
    void contextLoads() {
    }

}
