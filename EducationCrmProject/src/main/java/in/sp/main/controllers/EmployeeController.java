package in.sp.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.dto.EmployeeProfileDTO;
import in.sp.main.entities.Employee;
import in.sp.main.entities.EmployeeOrder;
import in.sp.main.entities.Inquiry;
import in.sp.main.entities.Orders;
import in.sp.main.repositories.EmployeeOrderRepository;
import in.sp.main.services.CourseService;
import in.sp.main.services.EmployeeAdminService;
import in.sp.main.services.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeAdminService employeeAdminService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private EmployeeOrderRepository employeeOrderRepository;

	@GetMapping("/employeeLogin")
	public String openEmployeeLoginPage(Model model, HttpSession session) {
		if (session.getAttribute("sessionEmployee") != null) {
			return "redirect:/employeeProfile";
		}
		model.addAttribute("employee", new Employee());
		return "employee-login-page";
	}

	@PostMapping("/employeeLoginForm")
	public String employeeLoginForm(@ModelAttribute("employee") Employee employee, HttpSession session,
			HttpServletRequest request, Model model) {
		try {
			if (employee.getEmailId() == null || employee.getEmailId().trim().isEmpty()) {
				model.addAttribute("errorMsg", "Email is required");
				model.addAttribute("employee", new Employee());
				return "employee-login-page";
			}

			if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
				model.addAttribute("errorMsg", "Password is required");
				model.addAttribute("employee", new Employee());
				return "employee-login-page";
			}

			if (employeeAdminService.login(employee.getEmailId(), employee.getPassword())) {
				Employee loggedIn = employeeAdminService.getEmployeeDetails(employee.getEmailId());

				if (loggedIn == null) {
					model.addAttribute("errorMsg", "Employee not found");
					model.addAttribute("employee", new Employee());
					return "employee-login-page";
				}

				session.invalidate();
				HttpSession newSession = request.getSession(true);

				newSession.setAttribute("sessionEmployee", loggedIn);
				newSession.setMaxInactiveInterval(30 * 60);

				return "redirect:/employeeProfile";
			} else {
				model.addAttribute("errorMsg", "Incorrect Email or Password");
				model.addAttribute("employee", new Employee());
				return "employee-login-page";
			}
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Login failed. Please try again.");
			model.addAttribute("employee", new Employee());
			return "employee-login-page";
		}
	}

	@GetMapping("/employeeProfile")
	public String openProfilePage(HttpSession session, Model model) {
		Employee loginEmployee = (Employee) session.getAttribute("sessionEmployee");
		if (loginEmployee == null) {
			return "redirect:/employeeLogin";
		}

		model.addAttribute("sessionEmployee", loginEmployee);

		try {
			EmployeeProfileDTO profile = employeeAdminService.getEmployeeProfile(loginEmployee.getEmailId());

			if (profile == null) {
				profile = new EmployeeProfileDTO(loginEmployee.getName() != null ? loginEmployee.getName() : "N/A",
						loginEmployee.getEmailId() != null ? loginEmployee.getEmailId() : "N/A",
						loginEmployee.getPhoneNo() != null ? loginEmployee.getPhoneNo() : "N/A",
						loginEmployee.getCity() != null ? loginEmployee.getCity() : "N/A");
			}

			model.addAttribute("employeeProfile", profile);
			return "employee-profile-page";
		} catch (Exception e) {
			e.printStackTrace();
			EmployeeProfileDTO profile = new EmployeeProfileDTO(
					loginEmployee.getName() != null ? loginEmployee.getName() : "N/A",
					loginEmployee.getEmailId() != null ? loginEmployee.getEmailId() : "N/A",
					loginEmployee.getPhoneNo() != null ? loginEmployee.getPhoneNo() : "N/A",
					loginEmployee.getCity() != null ? loginEmployee.getCity() : "N/A");
			model.addAttribute("employeeProfile", profile);
			model.addAttribute("errorMsg", "Failed to load complete profile");
			return "employee-profile-page";
		}
	}

	@GetMapping("/employeeLogout")
	public String employeeLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/employeeLogin";
	}

	@GetMapping("/sellCourse")
	public String openSellCoursePage(Model model, HttpSession session) {
		Employee employee = (Employee) session.getAttribute("sessionEmployee");
		if (employee == null) {
			return "redirect:/employeeLogin";
		}

		model.addAttribute("sessionEmployee", employee);

		try {
			List<String> courseNameList = courseService.getAllCourseName();
			model.addAttribute("courseNameList", courseNameList);

			String uuiorderId = UUID.randomUUID().toString();
			model.addAttribute("uuiorderId", uuiorderId);

			model.addAttribute("orders", new Orders());

			return "sell-course";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load sell course page");
			return "sell-course";
		}
	}

	@PostMapping("/sellCourseForm")
	public String sellCourseForm(@ModelAttribute("orders") Orders orders, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Employee sessionEmployee = (Employee) session.getAttribute("sessionEmployee");
		if (sessionEmployee == null) {
			return "redirect:/employeeLogin";
		}

		try {
			LocalDate ld = LocalDate.now();
			String pdate = ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			LocalTime lt = LocalTime.now();
			String ptime = lt.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

			String purchased_date_time = pdate + ", " + ptime;
			orders.setDateofPurchase(purchased_date_time);

			ordersService.storeUserOrders(orders);

			EmployeeOrder employeeOrder = new EmployeeOrder();
			employeeOrder.setOrderId(orders.getOrderId());
			employeeOrder.setEmployeeEmail(sessionEmployee.getEmailId());

			employeeOrderRepository.save(employeeOrder);

			redirectAttributes.addFlashAttribute("successMsg", "Course sold successfully");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to sell course. Please try again.");
		}

		return "redirect:/sellCourse";
	}

	@GetMapping("/inquiryMangement")
	public String openInquiryMangementPage(HttpSession session, Model model) {
		Employee employee = (Employee) session.getAttribute("sessionEmployee");
		if (employee == null) {
			return "redirect:/employeeLogin";
		}

		model.addAttribute("sessionEmployee", employee);

		model.addAttribute("inquiry", new Inquiry());
		return "inquiry-mangement";
	}
}