package fr.soat.training.api.superhero.web;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIsBaseComponentTest {

    @LocalServerPort
    private int port;

    protected RequestSpecification given() {
        return RestAssured.given()
                .port(port)
                .basePath("/api/v1/");
    }

}
