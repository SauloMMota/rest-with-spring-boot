package br.com.smms.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.smms.exceptions.ResourceNotFoundException;
import br.com.smms.model.Person;
import br.com.smms.services.PersonService;

@WebMvcTest
class PersonControllerTest {

	private static final long  PERSON_ID = 1L;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PersonService personService;
	
	private Person person, person2;
	private List<Person> people;
	
	@BeforeEach
	void setUp() throws Exception {
		
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
		
		people = List.of(person, person2);
	}
	
	// test[System Under Test]_[Condition or State Change]_[Expected Result]	
	@Test
	@DisplayName("test Given Person Object When Create Person then Return Saved Person")
	void testGivenPersonObject_WhenCreatePerson_thenReturnSavedPerson() throws Exception {
		// Given / Arrange
		given(personService.create(any(Person.class)))
			.willAnswer((invocation) -> invocation.getArgument(0));
		// When / Act
		ResultActions response = mockMvc.perform(post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(person)));
		
		//	Then / Assert
		response.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
			.andExpect(jsonPath("$.email", is(person.getEmail())));

	}
	
	@Test
	@DisplayName("test Given People List When FindAll Person then Return People List")
	void testGivenPeopleList_WhenFindAllPerson_thenReturnPeopleList() throws Exception {
		// Given / Arrange
		given(personService.findAll()).willReturn(people);
		// When / Act
		ResultActions response = mockMvc.perform(get("/person"));
		
		//	Then / Assert
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.size()", is(people.size())));

	}
	
	@Test
	@DisplayName("test Given Person Id When FindById then Return Person Object")
	void testGivenPersonId_WhenFindById_thenReturnPersonObject() throws Exception {
		// Given / Arrange

		given(personService.findById(PERSON_ID)).willReturn(person);
		
		// When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", PERSON_ID));
		
		//	Then / Assert
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
			.andExpect(jsonPath("$.email", is(person.getEmail())));
		
	}
	
	@Test
	@DisplayName("test Given Invalid Person Id When FindById then Return Not Found")
	void testGivenInvalidPersonId_WhenFindById_thenReturnNotFound() throws Exception {
		// Given / Arrange

		given(personService.findById(PERSON_ID)).willThrow(ResourceNotFoundException.class);
		
		// When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", PERSON_ID));
		
		//	Then / Assert
		response
			.andExpect(status().isNotFound())
			.andDo(print());
		
	}

	@Test
	@DisplayName("test Given Update Person When Update Person then Return Updated Person Object")
	void testGivenUpdatePerson_WhenUpdatePerson_thenReturnUpdatedPersonObject() throws Exception {
		// Given / Arrange
		person.setId(PERSON_ID);
		
		given(personService.findById(PERSON_ID)).willReturn(person);
		
		given(personService.update(any(Person.class)))
			.will((invocation) -> invocation.getArgument(0));

		Person personUpdated = new Person("First Updated Test", 
				"Last Updated Test",
				"Street  Updated Test",
				"Male",
				"emailUpdated@test.com");
		
		// When / Act
		ResultActions response = mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personUpdated)));

		// Then / Assert
		response.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(personUpdated.getFirstName())))
			.andExpect(jsonPath("$.email", is(personUpdated.getEmail())));

	}
	
	@Test
	@DisplayName("test Given Unexistent Person When Update Person then Return Not Found")
	void testGivenUnexistentPerson_WhenUpdatePerson_thenReturnNotFound() throws Exception {
		// Given / Arrange
		person.setId(PERSON_ID);
		given(personService.findById(PERSON_ID)).willThrow(ResourceNotFoundException.class);
		
		given(personService.update(any(Person.class)))
			.will((invocation) -> invocation.getArgument(1));
		
		Person personUpdated = new Person("First Updated Test", 
				"Last Updated Test",
				"Street  Updated Test",
				"Male",
				"emailUpdated@test.com");
		
		// When / Act
		ResultActions response = mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(personUpdated)));
		
		// Then / Assert
		response.andExpect(status().isNotFound())
		.andDo(print());
		
	}
	
	@Test
	@DisplayName("test Given Person Id Person When Delete then Return No Content")
	void testGivenPersonId_WhenDelete_thenReturnNoContent() throws Exception {
		// Given / Arrange
		person.setId(PERSON_ID);
		
		given(personService.findById(PERSON_ID)).willReturn(person);
		
		willDoNothing().given(personService).delete(PERSON_ID);
		
		// When / Act
		ResultActions response = mockMvc.perform(delete("/person/{id}", person.getId()));

		// Then / Assert
		response.andExpect(status().isNoContent())
			.andDo(print());

	}
		
}
