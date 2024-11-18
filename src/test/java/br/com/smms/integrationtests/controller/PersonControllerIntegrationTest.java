package br.com.smms.integrationtests.controller;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.smms.config.TestConfigs;
import br.com.smms.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.smms.model.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;


@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static Person person;
	
	@BeforeAll
	public static void setUp() throws Exception {
		//Given / Arrage
		
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		specification = new RequestSpecBuilder()
				.setBasePath("/person")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		person = new Person("First Test", 
				"Last Test",
				"Street Test",
				"Male",
				"email@test.com");
	}
	

	@Test
	@Order(1)
	@DisplayName("integration Test Given Person Object When Create One Person Should Return A Person Object")
	void integrationTestGivenPersonObject_WhenCreateOnePerson_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
				.when()
					.post()
				.then()
					.statusCode(201)
				.extract()
					.body()
					.asString();
		
		Person createdPerson = objectMapper.readValue(content, Person.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertNotNull(createdPerson.getEmail());
		
		assertTrue(createdPerson.getId() > 0);
		assertEquals("First Test", createdPerson.getFirstName());
		assertEquals("Last Test", createdPerson.getLastName());
		assertEquals("Street Test", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertEquals("email@test.com", createdPerson.getEmail());
	}
	
	@Test
	@Order(2)
	@DisplayName("integration Test Given Person Object When Update One Person Should Return A Updated Person Object")
	void integrationTestGivenPersonObject_WhenUpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws JsonMappingException, JsonProcessingException {
		
		person.setFirstName("First Update Name");
		person.setEmail("email_update@test.com");
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(person)
				.when()
					.put()
				.then()
					.statusCode(200)
				.extract()
					.body()
					.asString();
		
		Person updatePerson = objectMapper.readValue(content, Person.class);
		
		person = updatePerson;
		
		assertNotNull(updatePerson);
		assertNotNull(updatePerson.getFirstName());
		assertNotNull(updatePerson.getLastName());
		assertNotNull(updatePerson.getId());
		assertNotNull(updatePerson.getAddress());
		assertNotNull(updatePerson.getGender());
		assertNotNull(updatePerson.getEmail());
		
		assertTrue(updatePerson.getId() > 0);
		assertEquals("First Update Name", updatePerson.getFirstName());
		assertEquals("Last Test", updatePerson.getLastName());
		assertEquals("Street Test", updatePerson.getAddress());
		assertEquals("Male", updatePerson.getGender());
		assertEquals("email_update@test.com", updatePerson.getEmail());
	}
	
	@Test
	@Order(3)
	@DisplayName("integration Test Given Person Object When FindById Should Return A Updated Person Object")
	void integrationTestGivenPersonObject_WhenFindById_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {

		
		var content = given().spec(specification)
				.pathParam("id", person.getId())
				.when()
					.get("/{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
					.asString();
		
		Person foundPerson = objectMapper.readValue(content, Person.class);
		
		assertNotNull(foundPerson);
		assertNotNull(foundPerson.getFirstName());
		assertNotNull(foundPerson.getLastName());
		assertNotNull(foundPerson.getId());
		assertNotNull(foundPerson.getAddress());
		assertNotNull(foundPerson.getGender());
		assertNotNull(foundPerson.getEmail());
		
		assertTrue(foundPerson.getId() > 0);
		assertEquals("First Update Name", foundPerson.getFirstName());
		assertEquals("Last Test", foundPerson.getLastName());
		assertEquals("Street Test", foundPerson.getAddress());
		assertEquals("Male", foundPerson.getGender());
		assertEquals("email_update@test.com", foundPerson.getEmail());
	}
	
	@Test
	@Order(4)
	@DisplayName("integration Test Given Person Object When FindAll Should Return A People List Object")
	void integrationTestGivenPersonObject_WhenFindAll_ShouldReturnAPeopleListObject() throws JsonMappingException, JsonProcessingException {
		
		Person person2 = new Person("First Test 2", 
				"Last Test 2",
				"Street Test 2",
				"Male 2",
				"email@test.com 2");
		
		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
		.body(person2)
		.when()
			.post()
		.then()
			.statusCode(201);
		
		var content = given().spec(specification)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
					.asString();
		
		Person[] myArray = objectMapper.readValue(content, Person[].class);
		List<Person> people = Arrays.asList(myArray);
		
		Person foundPerson = people.get(0);
		
		assertTrue(people.size() > 0);
		
		assertTrue(foundPerson.getId() > 0);
		assertEquals("First Update Name", foundPerson.getFirstName());
		assertEquals("Last Test", foundPerson.getLastName());
		assertEquals("Street Test", foundPerson.getAddress());
		assertEquals("Male", foundPerson.getGender());
		assertEquals("email_update@test.com", foundPerson.getEmail());
		
		Person foundPersonTwo = people.get(1);
		assertEquals("First Test 2", foundPersonTwo.getFirstName());
		assertEquals("Last Test 2", foundPersonTwo.getLastName());
		assertEquals("Street Test 2", foundPersonTwo.getAddress());
		assertEquals("Male 2", foundPersonTwo.getGender());
		assertEquals("email@test.com 2", foundPersonTwo.getEmail());
	}
	
	@Test
	@Order(5)
	@DisplayName("integration Test Given Person Object When Delete Should Return No Content")
	void integrationTestGivenPersonObject_WhenDelete_ShouldReturnNoContent() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.pathParam("id", person.getId())
			.when()
				.delete("/{id}")
			.then()
				.statusCode(204);
				
	}

}
