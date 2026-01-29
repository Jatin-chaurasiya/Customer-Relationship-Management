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

import in.sp.main.entities.Admin;
import in.sp.main.entities.Employee;
import in.sp.main.services.EmployeeAdminService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeAdminController {

	@Autowired
	private EmployeeAdminService employeeService;

	@GetMapping("/employeeMangement")
	public String openEmployeeMangementPage(Model model, HttpSession session,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			PageRequest pageable = PageRequest.of(page, size);
			Page<Employee> employeePage = employeeService.getEmployeeDetailsByPagination(pageable);

			model.addAttribute("employeePage", employeePage);
			return "employee-mangement-page";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load employees");
			return "employee-mangement-page";
		}
	}

	@GetMapping("/addEmployee")
	public String openAddEmployeePage(Model model, HttpSession session) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		model.addAttribute("employee", new Employee());
		return "add-employee";
	}

	@PostMapping("/addEmployeeForm")
	public String addEmployeeForm(@ModelAttribute("employee") Employee employee, HttpSession session, Model model) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			if (employee.getName() == null || employee.getName().trim().isEmpty()) {
				model.addAttribute("errorMsg", "Employee name is required");
				model.addAttribute("employee", employee);
				return "add-employee";
			}

			if (employee.getEmailId() == null || employee.getEmailId().trim().isEmpty()) {
				model.addAttribute("errorMsg", "Email is required");
				model.addAttribute("employee", employee);
				return "add-employee";
			}

			employeeService.addEmployee(employee);
			model.addAttribute("successMsg", "Employee added successfully");
			model.addAttribute("employee", new Employee());
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMsg", e.getMessage());
			model.addAttribute("employee", employee);
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to add employee: " + e.getMessage());
			model.addAttribute("employee", employee);
		}
		return "add-employee";
	}

	@GetMapping("/editEmployee")
	public String openEditEmployeePage(@RequestParam("employeeEmail") String employeeEmail, HttpSession session,
			Model model) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			Employee employee = employeeService.getEmployeeDetails(employeeEmail);

			if (employee == null) {
				model.addAttribute("errorMsg", "Employee not found");
				return "redirect:/employeeMangement";
			}

			model.addAttribute("employee", employee);
			model.addAttribute("newEmployeeObj", new Employee());
			return "edit-employee";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load employee");
			return "redirect:/employeeMangement";
		}
	}

	@PostMapping("/updateEmployeeDetailsForm")
	public String updateEmployeeDetailsForm(@ModelAttribute("newEmployeeObj") Employee newEmployeeObj,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			Employee oldEmployee = employeeService.getEmployeeDetails(newEmployeeObj.getEmailId());

			if (oldEmployee == null) {
				redirectAttributes.addFlashAttribute("errorMsg", "Employee not found");
				return "redirect:/employeeMangement";
			}

			newEmployeeObj.setId(oldEmployee.getId());
			employeeService.updateEmployeeDetails(newEmployeeObj);

			redirectAttributes.addFlashAttribute("successMsg", "Employee updated successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to update employee: " + e.getMessage());
		}

		return "redirect:/employeeMangement";
	}

	@GetMapping("/deleteEmployeeDetails")
	public String deleteEmployeeDetailPage(@RequestParam("employeeEmail") String employeeEmail, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			employeeService.deleteEmployeeDetails(employeeEmail);
			redirectAttributes.addFlashAttribute("successMsg", "Employee deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to delete employee: " + e.getMessage());
		}
		return "redirect:/employeeMangement";
	}
}