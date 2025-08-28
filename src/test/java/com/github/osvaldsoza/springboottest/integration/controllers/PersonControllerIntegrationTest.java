package com.github.osvaldsoza.springboottest.integration.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.osvaldsoza.springboottest.config.ContentPortConfig;
import com.github.osvaldsoza.springboottest.integration.testcontainers.AbstractIntegrationTest;
import com.github.osvaldsoza.springboottest.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification request;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    public static void setupSpec() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        request = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(ContentPortConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");
    }

    @Test
    @Order(1)
    void createPersonTest() throws JsonProcessingException {
        var content = given()
                .spec(request)
                .contentType(ContentPortConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());

        assertEquals(person.getFirstName(), createdPerson.getFirstName());
    }

    @Test
    @Order(2)
    void testABC_When_XYZ_Should() throws JsonProcessingException {

        person.setFirstName("Osvaldo de");
        person.setLastName("Souza Ferreira");
        person.setEmail("osvaldo.ferreira@gmail.com");

        var content = given()
                .spec(request)
                .contentType(ContentPortConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person updatedPerson = objectMapper.readValue(content, Person.class);

        assertEquals(person.getFirstName(), updatedPerson.getFirstName());
        assertEquals(person.getLastName(), updatedPerson.getLastName());
        assertEquals(person.getEmail(), updatedPerson.getEmail());

    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = given()
                .spec(request)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person foundedPerson = objectMapper.readValue(content, Person.class);
        assertEquals("Osvaldo de", foundedPerson.getFirstName());
        assertEquals("Souza Ferreira", foundedPerson.getLastName());
        assertEquals("osvaldo.ferreira@gmail.com", foundedPerson.getEmail());
    }

    @Test
    @Order(4)
    void createAnotherPersonTest() throws JsonProcessingException {
        Person anotherPerson = new Person("Andreia", "Souza Silva", "silva@gmail.com", "Blumenau", "Female");


        var content = given()
                .spec(request)
                .contentType(ContentPortConfig.CONTENT_TYPE_JSON)
                .body(anotherPerson)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());

        assertEquals(anotherPerson.getFirstName(), createdPerson.getFirstName());
    }

    @Test
    @Order(5)
    void findByAllTest() throws JsonProcessingException {
        var content = given()
                .spec(request)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person[] arrayPerson = objectMapper.readValue(content,  Person[].class);
        List<Person> persons = Arrays.asList(arrayPerson);

        Person foundedPerson = persons.get(1);
        assertTrue(persons.size() == 2);
        assertEquals("Andreia", foundedPerson.getFirstName());
        assertEquals("Souza Silva", foundedPerson.getLastName());
        assertEquals("silva@gmail.com", foundedPerson.getEmail());

    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {
        given()
                .spec(request)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

}
