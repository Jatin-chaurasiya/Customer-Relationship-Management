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
		if (employee == null || employee.getEmailId() == null || employee.getEmailId().trim().isEmpty()) {
			throw new IllegalArgumentException("Employee email is required");
		}

		if (employeeRepository.existsByEmailId(employee.getEmailId().trim())) {
			throw new IllegalArgumentException("Employee already exists with this email");
		}

		if (employee.getName() == null || employee.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Employee name is required");
		}

		if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
			throw new IllegalArgumentException("Password is required");
		}

		employeeRepository.save(employee);
	}

	public Employee getEmployeeDetails(String employeeEmail) {
		if (employeeEmail == null || employeeEmail.trim().isEmpty()) {
			return null;
		}
		return employeeRepository.findByEmailId(employeeEmail.trim());
	}

	public Page<Employee> getEmployeeDetailsByPagination(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}

	public void updateEmployeeDetails(Employee employee) {
		if (employee == null || employee.getId() == null) {
			throw new IllegalArgumentException("Invalid employee data");
		}
		employeeRepository.save(employee);
	}

	public void deleteEmployeeDetails(String employeeEmail) {
		if (employeeEmail == null || employeeEmail.trim().isEmpty()) {
			throw new IllegalArgumentException("Employee email is required");
		}

		Employee employee = employeeRepository.findByEmailId(employeeEmail.trim());
		if (employee != null) {
			employeeRepository.delete(employee);
		} else {
			throw new RuntimeException("Employee not found with email: " + employeeEmail);
		}
	}

	public EmployeeProfileDTO getEmployeeProfile(String email) {
		List<Object[]> result = employeeRepository.findEmployeeProfileByEmail(email);
		if (result.isEmpty()) {
			return null;
		}

		Object[] row = result.get(0);
		return new EmployeeProfileDTO((String) row[0], (String) row[1], (String) row[2], (String) row[3]);
	}

	public boolean login(String email, String password) {
		if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
			return false;
		}

		Employee employee = employeeRepository.findByEmailId(email.trim());
		return employee != null && employee.getPassword().equals(password);
	}
}