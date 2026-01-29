package in.sp.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.sp.main.entities.Employee;
import jakarta.servlet.http.HttpSession;

@Controller
public class FollowUpsController {

	@GetMapping("/followUps")
	public String openFolloUpPage(HttpSession session, Model model) {
		Employee employee = (Employee) session.getAttribute("sessionEmployee");
		if (employee == null) {
			return "redirect:/employeeLogin";
		}

		model.addAttribute("sessionEmployee", employee);

		return "follow-ups";
	}
}