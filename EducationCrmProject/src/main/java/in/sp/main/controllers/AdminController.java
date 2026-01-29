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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Admin;
import in.sp.main.entities.Feedback;
import in.sp.main.repositories.AdminRepository;
import in.sp.main.services.AdminService;
import in.sp.main.services.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private FeedbackService feedbackService;

	@GetMapping("/adminLogin")
	public String openAdminLoginPage(Model model, HttpSession session) {
		if (session.getAttribute("sessionAdmin") != null) {
			return "redirect:/adminProfile";
		}
		model.addAttribute("admin", new Admin());
		return "admin-login-page";
	}

	@PostMapping("/adminLoginForm")
	public String adminLoginForm(@ModelAttribute("admin") Admin admin, HttpSession session, HttpServletRequest request,
			Model model) {
		try {
			boolean isAuthenticated = adminService.adminLoginService(admin.getEmail(), admin.getPassword());

			if (isAuthenticated) {
				Admin authenticatedUser = adminRepository.findByEmail(admin.getEmail());

				if (authenticatedUser == null) {
					model.addAttribute("errorMsg", "User not found");
					model.addAttribute("admin", new Admin());
					return "admin-login-page";
				}

				session.invalidate();
				HttpSession newSession = request.getSession(true);

				newSession.setAttribute("sessionAdmin", authenticatedUser);
				newSession.setMaxInactiveInterval(30 * 60);

				return "redirect:/adminProfile";
			} else {
				model.addAttribute("errorMsg", "Incorrect Email or Password");
				model.addAttribute("admin", new Admin());
				return "admin-login-page";
			}
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Login failed. Please try again.");
			model.addAttribute("admin", new Admin());
			return "admin-login-page";
		}
	}

	@GetMapping("/adminLogout")
	public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate();
		redirectAttributes.addFlashAttribute("successMsg", "Logged out successfully");
		return "redirect:/adminLogin";
	}

	@GetMapping("/adminFeedback")
	public String openFeedbackPage(Model model, HttpSession session,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			PageRequest pageable = PageRequest.of(page, size);
			Page<Feedback> feedbackPage = feedbackService.getAllFeedbacksByPagination(pageable);

			model.addAttribute("feedbackPage", feedbackPage);
			model.addAttribute("currentAdmin", admin);

			return "view-feedbacks";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load feedbacks");
			return "view-feedbacks";
		}
	}

	@PostMapping("/updateFeedbackStatus")
	@ResponseBody
	public String updateFeedbackStatus(@RequestParam("id") Long id, @RequestParam("status") String status,
			HttpSession session) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "unauthorized";
		}

		try {
			boolean success = feedbackService.updateFeedbackStatus(id, status);
			return success ? "success" : "fail";
		} catch (Exception e) {
			return "error";
		}
	}

}