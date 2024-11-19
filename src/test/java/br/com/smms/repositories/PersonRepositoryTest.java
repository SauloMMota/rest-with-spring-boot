package br.com.smms.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.smms.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.smms.model.Person;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private PersonRepository personRepository;
	
	private Person person;
	
	@BeforeEach
	public void setUp() {
		//Given / Arrage
		person = new Person("First Test", 
				"Last Test",
				"Street Test",
				"Male",
				"email@test.com");
		
	}
	
	@Test
	@DisplayName("Given Person Object when Save then Return Saved Person")
	void testGivenPersonObject_whenSave_thenReturnSavedPerson() {
		
		// When / Act
		Person savedPerson = personRepository.save(person);
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
	}
	
	@Test
	@DisplayName("Given Person List when FindAll then Return Person List")
	void testGivenPersonList_whenFindAll_thenReturnPersonList() {
		// Given / Arrange
		
		Person person2 = new Person("First Test 2", 
				"Last Test 2",
				"Street Test 2",
				"Female",
				"email2@test.com");
		
		personRepository.saveAll(List.of(person, person2));
		// When / Act
		var savedPerson = personRepository.findAll();
		//	Then / Assert
		assertNotNull(savedPerson);
	}
	
	@Test
	@DisplayName("Given Person Object when FindByID then Return Person Object")
	void testGivenPersonObject_whenFindById_thenReturnPersonObject() {
		// Given / Arrange
		
		personRepository.save(person);
		// When / Act
		var savedPerson = personRepository.findById(person.getId()).get();
		
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
		assertEquals(savedPerson.getId(), person.getId());
	}
	
	
	@Test
	@DisplayName("Given Person Object when FindByEmail then Return Person Object")
	void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
		// Given / Arrange
		personRepository.save(person);		
		// When / Act
		var savedPerson = personRepository.findByEmail(person.getEmail()).get();
		
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
		assertEquals(savedPerson.getEmail(), person.getEmail());
	}
	
	@Test
	@DisplayName("Given Person Object when Update Person then Return Updated Person Object")
	void testGivenPersonObject_whenUpdatePerson_thenReturnUpadatedPersonObject() {
		// Given / Arrange
		
		personRepository.save(person);
		// When / Act
		var savedPerson = personRepository.findByEmail(person.getEmail()).get();
		savedPerson.setFirstName("João");
		savedPerson.setEmail("jao@email.com");
		var updatedPerson = personRepository.save(savedPerson);
		//	Then / Assert
		assertNotNull(updatedPerson);
		assertEquals(savedPerson.getFirstName(), person.getFirstName());
		assertEquals(savedPerson.getEmail(), person.getEmail());
	}
	
	@Test
	@DisplayName("Given Person Object when Delete then Remove Person")
	void testGivenPersonObject_whenDelete_thenRremovePerson() {
		// Given / Arrange

		personRepository.save(person);
		// When / Act	
		personRepository.deleteById(person.getId());
		Optional<Person> savedPerson = personRepository.findByEmail(person.getEmail());
		//	Then / Assert
		assertTrue(savedPerson.isEmpty());
	}
	
	@Test
	@DisplayName("Given Person Object when FindByJPQL then Return Person Object")
	void testGivenPersonObject_whenFindByJPQL_thenReturnPersonObject() {
		// Given / Arrange
		
		personRepository.save(person);
		// When / Act
		var savedPerson = personRepository.findByJPQL(person.getFirstName(), person.getLastName());
		
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
		assertEquals(savedPerson.getId(), person.getId());
	}
	
	@Test
	@DisplayName("Given Person Object when FindByJPQLNamedParameters then Return Person Object")
	void testGivenPersonObject_whenFindByJPQLNamedParameters_thenReturnPersonObject() {
		// Given / Arrange
		
		personRepository.save(person);
		// When / Act
		var savedPerson = personRepository.findByJPQLNamedParameters(person.getFirstName(), person.getLastName());
		
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
		assertEquals(savedPerson.getId(), person.getId());
	}
	
	@Test
	@DisplayName("Given Person Object when FindByNativeSQL then Return Person Object")
	void testGivenPersonObject_whenFindByNativeSQL_thenReturnPersonObject() {
		// Given / Arrange
		
		personRepository.save(person);
		// When / Act
		var savedPerson = personRepository.findByNativeSQL(person.getFirstName(), person.getLastName());
		
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
		assertEquals(savedPerson.getId(), person.getId());
	}
	
	@Test
	@DisplayName("Given Person Object when FindByNativeSQLNamedParameters then Return Person Object")
	void testGivenPersonObject_whenFindByNativeSQLNamedParameters_thenReturnPersonObject() {
		// Given / Arrange
		
		personRepository.save(person);
		// When / Act
		var savedPerson = personRepository.findByNativeSQLNamedParameters(person.getFirstName(), person.getLastName());
		
		//	Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
		assertEquals(savedPerson.getId(), person.getId());
	}
}
