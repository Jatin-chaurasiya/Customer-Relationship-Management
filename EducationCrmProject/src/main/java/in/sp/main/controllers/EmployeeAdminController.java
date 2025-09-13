package in.sp.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Employee;
import in.sp.main.services.EmployeeAdminService;

@Controller
public class EmployeeAdminController {

	@Autowired
	private EmployeeAdminService employeeService;

	// ---------- Show Employee Management Page with Pagination ----------
	@GetMapping("/employeeMangement")
	public String openEmployeeMangementPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {

		PageRequest pageable = PageRequest.of(page, size);
		Page<Employee> employeePage = employeeService.getEmployeeDetailsByPagination(pageable);

		model.addAttribute("employeePage", employeePage);
		return "employee-mangement-page";
	}

	// ---------- Add Employee ----------
	@GetMapping("/addEmployee")
	public String openAddEmployeePage(Model model) {
		model.addAttribute("employee", new Employee());
		return "add-employee";
	}

	@PostMapping("/addEmployeeForm")
	public String openAddCoursePage(@ModelAttribute("employee") Employee employee, Model model) {
		try {
			employeeService.addEmployee(employee);
			model.addAttribute("successMsg", "Employee added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Employee not added due to some error");
		}
		return "add-employee";
	}

	// ---------- Edit Employee ----------
	@GetMapping("/editEmployee")
	public String openEditEmployeePage(@RequestParam("employeeEmail") String employeeEmail, Model model) {
		Employee employee = employeeService.getEmployeeDetails(employeeEmail);
		model.addAttribute("employee", employee);
		model.addAttribute("newEmployeeObj", new Employee());
		return "edit-employee";
	}

	@PostMapping("/updateEmployeeDetailsForm")
	public String UpdateCourseDetailsForm(@ModelAttribute("newEmployeeObj") Employee newEmployeeObj,
			RedirectAttributes redirectAttributes) {

		Employee oldEmployee = employeeService.getEmployeeDetails(newEmployeeObj.getEmailId());
		newEmployeeObj.setId(oldEmployee.getId());

		try {
			employeeService.updateCourseDetails(newEmployeeObj);
			redirectAttributes.addFlashAttribute("successMsg", "Employee Details updated successfully..");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Employee Detail not updated due to some error..");
		}

		return "redirect:/employeeMangement";
	}

	// ---------- Delete Employee ----------
	@GetMapping("/deleteEmployeeDetails")
	public String deleteEmployeeDetailPage(@RequestParam("employeeEmail") String employeeEmail,
			RedirectAttributes redirectAttributes) {
		try {
			employeeService.deleteEmployeeDetails(employeeEmail);
			redirectAttributes.addFlashAttribute("successMsg", "Employee deleted successfully..");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/employeeMangement";
	}

}
