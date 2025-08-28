package com.github.osvaldsoza.springboottest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.osvaldsoza.springboottest.exceptions.ResourceNotFoundException;
import com.github.osvaldsoza.springboottest.model.Person;
import com.github.osvaldsoza.springboottest.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonService personService;

    Person person = new Person();

    @BeforeEach
    void setUP() {
        person = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");
    }

    //test[System Under Test]_[Condition or State Change]_[Expected Result]
    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Should() throws Exception {

        //Given / Arrange
        given(personService.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        //When / Act
        ResultActions resultActions = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));
        //Then / Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Should1() throws Exception {

        var person1 = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person1);

        //Given / Arrange
        given(personService.findAll()).willReturn(persons);
        //When / Act
        ResultActions resultActions = mockMvc.perform(get("/person"));
        //Then / Assert
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())));
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Sh11ould() throws Exception {

        long id =1L;
        //Given / Arrange
        given(personService.findById(id)).willReturn(person);
        //When / Act
        ResultActions resultActions = mockMvc.perform(get("/person/{id}", id));
        //Then / Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Sh11o11ld() throws Exception {

        long id =1L;
        //Given / Arrange
        given(personService.findById(id)).willThrow(ResourceNotFoundException.class);
        //When / Act
        ResultActions resultActions = mockMvc.perform(get("/person/{id}", id));
        //Then / Assert
        resultActions
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Sh1111ould() throws Exception {

        long id =1L;
        //Given / Arrange
        given(personService.findById(id)).willReturn(person);
        given(personService.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        //When / Act
        var person1 = new Person("Osvaldo111", "Ferreira111", "soza@gmail.com", "Blumenau", "Male");


        ResultActions resultActions = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person1)));
        //Then / Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person1.getLastName())))
                .andExpect(jsonPath("$.email", is(person1.getEmail())));
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Sh1111o11ld() throws Exception {

        long id =1L;
        //Given / Arrange
        given(personService.findById(id)).willThrow(ResourceNotFoundException.class);
        given(personService.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(1));
        //When / Act

        var person1 = new Person("Osvaldo111", "Ferreira111", "soza@gmail.com", "Blumenau", "Male");


        ResultActions resultActions = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person1)));

        //Then / Assert
        resultActions
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Sh111111o11ld() throws Exception {

        long id =1L;
        //Given / Arrange
        willDoNothing().given(personService).delete(id);
        //When / Act

        ResultActions resultActions = mockMvc.perform(delete("/person/{id}",id));

        //Then / Assert
        resultActions
                .andExpect(status().isNoContent())
                .andDo(print());
    }



}
