package in.sp.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.dto.EmployeeProfileDTO;
import in.sp.main.entities.Employee;
import in.sp.main.repositories.EmployeeAdminRepository;

@Service
public class EmployeeAdminService {

	@Autowired
	private EmployeeAdminRepository employeeRepository;

	public List<Employee> getAllEmployeeDetails() {
		return employeeRepository.findAll();
	}

	public void addEmployee(Employee employee) {
		employeeRepository.save(employee);
	}

	public Employee getEmployeeDetails(String employeeEmail) {
		return employeeRepository.findByEmailId(employeeEmail);
	}

	public Page<Employee> getEmployeeDetailsByPagination(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}

	public void updateCourseDetails(Employee employee) {
		employeeRepository.save(employee);
	}

	public void deleteEmployeeDetails(String employeeEmail) {
		Employee employee = employeeRepository.findByEmailId(employeeEmail);
		if (employee != null) {
			employeeRepository.delete(employee);
		} else {
			throw new RuntimeException("Employee not found with email : " + employeeEmail);
		}
	}

	// --- For Employee login/profile ---
	public EmployeeProfileDTO getEmployeeProfile(String email) {
		List<Object[]> result = employeeRepository.findEmployeeProfileByEmail(email);
		if (result.isEmpty())
			return null;

		Object[] row = result.get(0);
		return new EmployeeProfileDTO((String) row[0], // name
				(String) row[1], // email_id
				(String) row[2], // phone_no
				(String) row[3] // city
		);
	}

	// --- Login check ---
	public boolean login(String email, String password) {
		Employee employee = employeeRepository.findByEmailId(email);
		return employee != null && employee.getPassword().equals(password);
	}
}
