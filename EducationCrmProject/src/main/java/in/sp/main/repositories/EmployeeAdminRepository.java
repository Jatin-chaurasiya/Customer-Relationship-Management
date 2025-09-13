package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Employee;

@Repository
public interface EmployeeAdminRepository extends JpaRepository<Employee, Long> {

	Employee findByEmailId(String emailId); // For admin operations

	// Native query to fetch only selected fields
	String SELECT_QUERY = "SELECT name, email_id, phone_no, city FROM employee WHERE email_id = :email";

	@Query(value = SELECT_QUERY, nativeQuery = true)
	List<Object[]> findEmployeeProfileByEmail(@Param("email") String email);
}
