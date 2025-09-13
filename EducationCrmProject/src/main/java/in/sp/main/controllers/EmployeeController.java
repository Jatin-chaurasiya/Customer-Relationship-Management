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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
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
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("sessionEmployee")
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
	public String openEmployeeLoginPage(Model model) {
		model.addAttribute("employee", new Employee());
		return "employee-login-page";
	}

	@PostMapping("/employeeLoginForm")
	public String employeeLoginForm(@ModelAttribute("employee") Employee employee, Model model) {
		if (employeeAdminService.login(employee.getEmailId(), employee.getPassword())) {
			Employee loggedIn = employeeAdminService.getEmployeeDetails(employee.getEmailId());

			model.addAttribute("sessionEmployee", loggedIn);

			return "redirect:/employeeProfile";
		} else {
			model.addAttribute("errorMsg", "Incorrect Email or Password!");
			return "employee-login-page";
		}
	}

	@GetMapping("/employeeProfile")
	public String openProfilePage(@SessionAttribute("sessionEmployee") Employee loginEmployee, Model model) {
		EmployeeProfileDTO profile = employeeAdminService.getEmployeeProfile(loginEmployee.getEmailId());
		model.addAttribute("employeeProfile", profile);
		return "employee-profile-page";
	}

	@GetMapping("/employeeLogout")
	public String employeeLogout(HttpSession session, SessionStatus status) {
		status.setComplete();
		session.invalidate();
		return "redirect:/employeeLogin";
	}

	// -------------open sell course page------------------------
	@GetMapping("/sellCourse")
	public String openSellCoursePage(Model model, @SessionAttribute("sessionEmployee") Employee employee) {

		List<String> courseNameList = courseService.getAllCourseName();
		model.addAttribute("courseNameList", courseNameList);

		String uuiorderId = UUID.randomUUID().toString();
		model.addAttribute("uuiorderId", uuiorderId);

		model.addAttribute("orders", new Orders());

		return "sell-course";
	}

	@PostMapping("/sellCourseFoem")
	public String sellCourseFoem(@ModelAttribute("orders") Orders orders,
			@SessionAttribute("sessionEmployee") Employee sessionEmployee, RedirectAttributes redirectAttributes) {

		LocalDate ld = LocalDate.now();
		String pdate = ld.format(DateTimeFormatter.ofPattern("dd//MM//yyyy"));

		LocalTime lt = LocalTime.now();
		String ptime = lt.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));

		String purchased_date_time = pdate + ", " + ptime;
		orders.setDateofPurchase(purchased_date_time);

		try {
			ordersService.storeUserOrders(orders);

			EmployeeOrder employeeOrder = new EmployeeOrder();
			employeeOrder.setOrderId(orders.getOrderId());
			employeeOrder.setEmployeeEmail(sessionEmployee.getEmailId());

			employeeOrderRepository.save(employeeOrder);

			redirectAttributes.addFlashAttribute("successMsg", "Course provided successfully..");

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Course not  provided due to some error...");
		}

		return "redirect:/sellCourse";
	}

	// -------------Inquiry Management page------------------------
	@GetMapping("/inquiryMangement")
	public String openInquiryMangementPage(@SessionAttribute("sessionEmployee") Employee employee, Model model) {
		model.addAttribute("inquiry", new Inquiry());
		return "inquiry-mangement";
	}

}
