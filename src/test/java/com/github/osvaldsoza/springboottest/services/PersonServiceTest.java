package com.github.osvaldsoza.springboottest.services;

import com.github.osvaldsoza.springboottest.exceptions.ResourceNotFoundException;
import com.github.osvaldsoza.springboottest.model.Person;
import com.github.osvaldsoza.springboottest.repositories.PersonRepository;
import com.github.osvaldsoza.springboottest.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    Person person = new Person();

    @BeforeEach
    void setUP() {
        person = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");
    }

    //test[System Under Test]_[Condition or State Change]_[Expected Result]
    @Test
    @DisplayName("Display Name")
    void testSavePerson() {

        //Given / Arrange
        given(personRepository.findByEmail(person.getEmail())).willReturn(Optional.empty());
        given(personRepository.save(person)).willReturn(person);

        //When / Act
        var savedPerson = personService.create(person);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person.getFirstName(), savedPerson.getFirstName());
    }

    //test[System Under Test]_[Condition or State Change]_[Expected Result]
    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Should() {

        given(personRepository.findById(person.getId())).willReturn(Optional.of(person));

        var foundedPerson = personService.findById(person.getId());
        assertNotNull(foundedPerson);
        assertEquals(person.getId(), foundedPerson.getId());
    }

    //test[System Under Test]_[Condition or State Change]_[Expected Result]
    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_Should1() {

        given(personRepository.findById(person.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.findById(person.getId()));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    @DisplayName("Display Name")
    void testSavePersonException() {

        //Given / Arrange
        given(personRepository.findByEmail(person.getEmail())).willReturn(Optional.of(person));

        //When / Act
        assertThrows(ResourceNotFoundException.class, () -> personService.create(person));

        //Then / Assert
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    @DisplayName("Display Name")
    void testFindAllPerson() {

        //Given / Arrange
        given(personRepository.findAll()).willReturn(Collections.singletonList(person));

        //When / Act
        var list = personService.findAll();

        //Then / Assert
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Display Name")
    void testFindAllPerson1() {

        //Given / Arrange
        given(personRepository.findAll()).willReturn(Collections.emptyList());

        //When / Act
        var list = personService.findAll();

        //Then / Assert
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    //test[System Under Test]_[Condition or State Change]_[Expected Result]
    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ_111Should() {

        person.setId(1L);
        given(personRepository.findById(anyLong())).willReturn(Optional.of(person));
        person.setEmail("osvaldo.ferreira@gmail.com");
        person.setLastName("Souza");

        given(personRepository.save(person)).willReturn(person);

        var updatedPerson = personService.update(person);

        assertNotNull(updatedPerson);
        assertEquals(person.getEmail(), updatedPerson.getEmail());
        assertEquals(person.getLastName(), updatedPerson.getLastName());
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ111_111Should() {
        person.setId(1L);
        given(personRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.update(person));

        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    @DisplayName("Display Name")
    void testABC_When_XYZ111_111Sh11ould() {
        person.setId(1L);
        given(personRepository.findById(anyLong())).willReturn(Optional.of(person));
        willDoNothing().given(personRepository).delete(person);

       personService.delete(person.getId());

        verify(personRepository, times(1)).delete(any(Person.class));
    }
}
