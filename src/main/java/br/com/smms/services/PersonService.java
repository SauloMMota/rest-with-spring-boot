package br.com.smms.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.smms.exceptions.ResourceNotFoundException;
import br.com.smms.model.Person;
import br.com.smms.repositories.PersonRepository;

@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	@Autowired
	private PersonRepository personRepository;
	
	public List<Person> findAll() {
		logger.info("Finding all people");

		return personRepository.findAll();
	}

	public Person findById(Long id) {
		logger.info("Finding one person");
		return getPersonById(id);
	}
	
	public Person create(Person person) {
		logger.info("Creating one person!");
		
		Optional<Person> savedPerson = personRepository.findByEmail(person.getEmail());
		if(savedPerson.isPresent()) {
			throw new ResourceNotFoundException("Person already exist with given e-mail: " + person.getEmail());
		}
		return personRepository.save(person);
	}
	
	public Person update(Person person) {
		logger.info("Updating one person!");
		var entity = getPersonById(person.getId());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		entity.setEmail(person.getEmail());
		return personRepository.save(entity);
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = getPersonById(id);
		personRepository.delete(entity);
	}	
	
	private Person getPersonById(Long id) {
		return personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID!"));
	}
}
