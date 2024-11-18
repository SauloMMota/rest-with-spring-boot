package br.com.smms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.smms.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	Optional<Person> findByEmail(String email);

	// Define custom query using JPQL with index parameters
	@Query("FROM Person p WHERE p.firstName =?1 AND p.lastName =?2")
	Person findByJPQL(String firstName, String lastName);

	// Define custom query using JPQL with named parameters
	@Query("FROM Person p WHERE p.firstName =:firstName AND p.lastName =:lastName")
	Person findByJPQLNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);

	// Define custom query using Native SQL with index parameters
	@Query(value = "SELECT * FROM person p WHERE p.first_name =?1 AND p.last_name =?2", nativeQuery = true)
	Person findByNativeSQL(String firstName, String lastName);
	
	// Define custom query using Native SQL with named parameters
	@Query(value = "SELECT * FROM person p WHERE p.first_name =:firstName AND p.last_name =:lastName", nativeQuery = true)
	Person findByNativeSQLNamedParameters(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
