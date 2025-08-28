package com.github.osvaldsoza.springboottest.repositories;

import com.github.osvaldsoza.springboottest.integration.testcontainers.AbstractIntegrationTest;
import com.github.osvaldsoza.springboottest.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    Person person1 = new Person();

    @BeforeEach
    void setUP() {
        person1 = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");
    }

    //test[System Under Test]_[Condition or State Change]_[Expected Result]
    @Test
    @DisplayName("Display Name")
    void savePersonTest() {

        Person savedPerson = personRepository.save(person1);

        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @Test
    @DisplayName("Display Name")
    void listAllPersonTest() {
        Person person1 = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");

        personRepository.save(person1);

        var listPerson = personRepository.findAll();

        assertNotNull(listPerson);
        assertEquals(2, listPerson.size());
    }

    @Test
    @DisplayName("Display Name")
    void listPersonByIdTest() {

        personRepository.save(person1);

        var savedPerson = personRepository.findById(person1.getId()).get();

        assertNotNull(savedPerson);
        assertEquals(person1.getId(), savedPerson.getId());
    }

    @Test
    @DisplayName("Display Name")
    void listPersonByEmailTest() {
        personRepository.save(person1);

        var savedPerson = personRepository.findByEmail(person1.getEmail()).get();

        assertNotNull(savedPerson);
        assertEquals(person1.getEmail(), savedPerson.getEmail());
    }


    @Test
    void updatePersonTest() {
        Person person1 = new Person("Osvaldo", "Ferreira", "soza@gmail.com", "Blumenau", "Male");

        personRepository.save(person1);

        var savedPerson = personRepository.findById(person1.getId()).get();
        savedPerson.setEmail("osvald.soza@gmail.com");

        var updatedPerson = personRepository.save(savedPerson);

        assertEquals(savedPerson.getEmail(), updatedPerson.getEmail());
    }

    @Test
    void deletePersonTest() {
        personRepository.save(person1);

        personRepository.delete(person1);

        var person = personRepository.findById(person1.getId());

        assertTrue(person.isEmpty());
    }

    @Test
    void findPersonByFirtNameAndLastNameTest() {
        personRepository.save(person1);

        var person = personRepository.findByJPQL(person1.getFirstName(), person1.getLastName());

        assertNotNull(person);
        assertEquals(person1.getFirstName(), person.getFirstName());
        assertEquals(person1.getLastName(), person.getLastName());
    }

    @Test
    void findPersonByFirtNameAndLastName1Test() {
        personRepository.save(person1);

        var person = personRepository.findByJPQLNamedParameters(person1.getFirstName(), person1.getLastName());

        assertNotNull(person);
        assertEquals(person1.getFirstName(), person.getFirstName());
        assertEquals(person1.getLastName(), person.getLastName());
    }

    @Test
    void findPersonByFirtNameAndLastName2Test() {
        personRepository.save(person1);

        var person = personRepository.findByNativeSQL(person1.getFirstName(), person1.getLastName());

        assertNotNull(person);
        assertEquals(person1.getFirstName(), person.getFirstName());
        assertEquals(person1.getLastName(), person.getLastName());
    }

    @Test
    void findPersonByFirtNameAndLastName3Test() {
        personRepository.save(person1);

        var person = personRepository.findByNativeSQLwithNamedParameters(person1.getFirstName(), person1.getLastName());

        assertNotNull(person);
        assertEquals(person1.getFirstName(), person.getFirstName());
        assertEquals(person1.getLastName(), person.getLastName());
    }

}
