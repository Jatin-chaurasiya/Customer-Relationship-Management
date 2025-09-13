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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Admin;
import in.sp.main.entities.Feedback;
import in.sp.main.repositories.AdminRepository;
import in.sp.main.services.AdminService;
import in.sp.main.services.FeedbackService;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("sessionAdmin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private FeedbackService feedbackService;

	@GetMapping("/adminLogin")
	public String openAdminLoginPage(Model model) {

		model.addAttribute("admin", new Admin());
		return "admin-login-page";
	}

	@PostMapping("/adminLoginForm")
	public String adminLoginForm(@ModelAttribute("admin") Admin admin, HttpSession session, Model model) {
		boolean isAuthenticated = adminService.adminLoginService(admin.getEmail(), admin.getPassword());

		if (isAuthenticated) {
			Admin authenticatedUser = adminRepository.findByEmail(admin.getEmail());
			session.setAttribute("sessionAdmin", authenticatedUser); // store in session
			return "redirect:/adminProfile"; // redirect to dashboard
		} else {
			model.addAttribute("errorMsg", "Incorrect Email id and password");
			model.addAttribute("admin", new Admin());
			return "admin-login-page";
		}
	}

	@GetMapping("/adminLogout")
	public String logout(SessionStatus sessionStatus) {
		sessionStatus.setComplete(); // ✅ clears sessionAdmin
		return "redirect:/adminLogin";
	}

//------------------feedbackForm----------

	@GetMapping("/adminFeedback")
	public String openFeedbackPage(Model model, HttpSession session,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		PageRequest pageable = PageRequest.of(page, size);

		Page<Feedback> feedbackPage = feedbackService.getAllFeedbacksByPagination(pageable);

		model.addAttribute("feedbackPage", feedbackPage);

		return "veiw-feedbacks";
	}

	@PostMapping("/updateFeedbackStatus")
	public String updateFeedbackStatus(@RequestParam("id") Long id, @RequestParam("status") String status,
			RedirectAttributes redirectAttributes) {
		boolean success = feedbackService.updateFeedbackStatus(id, status);
		if (success) {
			redirectAttributes.addFlashAttribute("successMsg", "Feedback updated successfully.");
		} else {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to update feedback status.");
		}
		return "redirect:/adminFeedback";
	}

}
