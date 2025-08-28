package com.github.osvaldsoza.springboottest.integration.swagger;


import com.github.osvaldsoza.springboottest.config.ContentPortConfig;
import com.github.osvaldsoza.springboottest.integration.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("JUnit test for should Display Swagger UI Page")
    void testShouldDisplaySwaggerUiPage() {
        var content = given()
                .basePath("/swagger-ui/index.html")
                .port(ContentPortConfig.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        assertTrue(content.contains("Swagger UI"));
    }
}
