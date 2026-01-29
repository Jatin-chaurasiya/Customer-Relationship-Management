package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Employee;

@Repository
public interface EmployeeAdminRepository extends JpaRepository<Employee, Long> {

	Employee findByEmailId(String emailId);

	boolean existsByEmailId(String emailId);

	@Query(value = "SELECT name, email_id, phone_no, city FROM employee WHERE email_id = :email", nativeQuery = true)
	List<Object[]> findEmployeeProfileByEmail(@Param("email") String email);
}