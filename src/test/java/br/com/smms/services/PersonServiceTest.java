package br.com.smms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.smms.exceptions.ResourceNotFoundException;
import br.com.smms.model.Person;
import br.com.smms.repositories.PersonRepository;


@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

	@Mock
	private PersonRepository personRepository;
	
	@InjectMocks
	private PersonService personService;
	
	private Person person, person2;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		person = new Person("First Test", 
				"Last Test",
				"Street Test",
				"Male",
				"email@test.com");
		
		person2 = new Person("First Test 2", 
				"Last Test 2",
				"Street Test 2",
				"Male 2",
				"email@test.com 2");
		
	}
	
	@Test
	@DisplayName("test Given Person Object When Save Person then Return Person Object")
	void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
		// Given / Arrange
		given(personRepository.findByEmail(anyString())).willReturn(Optional.empty());
		
		given(personRepository.save(person)).willReturn(person);
		
		// When / Act
		Person savedPerson = personService.create(person);
		//	Then / Assert
		
		assertEquals("First Test", savedPerson.getFirstName());
		verify(personRepository, times(1)).save(any(Person.class));
	}
	

	@Test
	@DisplayName("test Given Existing Email When Save Person then Throws Exception")
	void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {
		// Given / Arrange
		given(personRepository.findByEmail(anyString())).willReturn(Optional.of(person));
		
		// When / Act
		ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> personService.create(person));
		//	Then / Assert
		assertTrue(result.getMessage().contains("Person already exist with given e-mail: " + person.getEmail()));
		verify(personRepository, never()).save(any(Person.class));
	}
	
	@Test
	@DisplayName("test Given Person List When Find All Person then Return Person List")
	void testGivenPersonList_WhenFindAllPerson_thenReturnPersonList() {
		// Given / Arrange
		given(personRepository.findAll()).willReturn(List.of(person, person2));
		
		// When / Act
		List<Person> people = personService.findAll();
		//	Then / Assert
		assertNotNull(people);
		assertFalse(people.isEmpty());
	}
	
	@Test
	@DisplayName("test Given Empty Person List When Find All Person then Return Empty Person List")
	void testGivenEmptyPersonList_WhenFindAllPerson_thenReturnEmptyPersonList() {
		// Given / Arrange
		given(personRepository.findAll()).willReturn(Collections.emptyList());
		
		// When / Act
		List<Person> people = personService.findAll();
		//	Then / Assert

		assertTrue(people.isEmpty());
		assertEquals(0, people.size());
	}
	
	@Test
	@DisplayName("test Given Person Id When FindById then Return Person Object")
	void testGivenPersonID_WhenFindById_thenReturnPersonObject() {
		// Given / Arrange
		given(personRepository.findById(anyLong())).willReturn(Optional.of(person));
		
		// When / Act
		Person savedPerson = personService.findById(1L);
		//	Then / Assert
		assertNotNull(savedPerson);
		assertEquals("First Test", savedPerson.getFirstName());
	}
	
	@Test
	@DisplayName("test Given Person Id When FindById then Throws Exception")
	void testGivenPersonID_WhenFindById_thenThrowsException() {
		// Given / Arrange
		given(personRepository.findById(anyLong())).willReturn(Optional.empty());
		
		// When / Act
		ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class,
				() -> personService.findById(1L));

		//	Then / Assert
		assertTrue(result.getMessage().contains("No records found this ID!"));
	}
	
	@Test
	@DisplayName("test Given Person Object When Update Person then Return Update Person Object")
	void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatePersonObject() {
		// Given / Arrange
		given(personRepository.findById(anyLong())).willReturn(Optional.of(person));
		
		person.setFirstName("First Update Test");
		person.setEmail("");
		person.setId(1L);
		
		given(personRepository.save(person)).willReturn(person);
		
		// When / Act
		Person updatedPerson = personService.update(person);
		//	Then / Assert
		assertNotNull(updatedPerson);
		assertEquals("First Update Test", updatedPerson.getFirstName());
		assertTrue(updatedPerson.getEmail().isEmpty());
	}
	
	@Test
	@DisplayName("test Given Person Id When Delete Person then Do nothing")
	void testGivenPersonId_WhenDeletePerson_thenDoNothing() {
		// Given / Arrange
		given(personRepository.findById(anyLong())).willReturn(Optional.of(person));

		person.setId(1L);
		
		willDoNothing().given(personRepository).delete(person);
		
		// When / Act
		personService.delete(person.getId());
		//	Then / Assert
		verify(personRepository, atLeastOnce()).delete(person);
	}
	
}
