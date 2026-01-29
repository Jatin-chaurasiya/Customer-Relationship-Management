package in.sp.main.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import in.sp.main.dto.PurchasedCourse;
import in.sp.main.entities.Course;
import in.sp.main.entities.User;
import in.sp.main.repositories.OrdersRepository;
import in.sp.main.repositories.UserRepository;
import in.sp.main.services.CourseService;
import in.sp.main.services.UserService;

@Controller
@SessionAttributes("sessionUser")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRespository;

	@Autowired
	private CourseService courseService;

	@Autowired
	private OrdersRepository ordersRepository;

	@GetMapping({ "/", "/index" })
	public String openIndexPage(Model model,
			@SessionAttribute(name = "sessionUser", required = false) User sessionUser) {
		List<Course> courseList = courseService.getAllCourseDetails();
		model.addAttribute("courseList", courseList);

		if (sessionUser != null) {
			List<Object[]> purchasedCourseList = ordersRepository.findPurchasedCourseByEmail(sessionUser.getEmail());

			List<String> purchasedCourseNameList = new ArrayList<>();
			for (Object[] course : purchasedCourseList) {
				String courseName = (String) (course[3]);
				purchasedCourseNameList.add(courseName);
			}
			model.addAttribute("purchasedCourseNameList", purchasedCourseNameList);
		}

		return "index";
	}

	@GetMapping("/register")
	public String openRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/regForm")
	public String handleRegisterForm(@ModelAttribute("user") User user, Model model) {
		userRespository.save(user);

		model.addAttribute("successMsg", "Registration Successful! Please login now.");

		return "register";
	}

	@GetMapping("/login")
	public String openLoginPage(Model model) {

		model.addAttribute("user", new User());
		return "login";
	}

	@PostMapping("/loginForm")
	public String handleLoginForm(@ModelAttribute("user") User user, Model model) {
		boolean isAuthenticated = userService.loginUserService(user.getEmail(), user.getPassword());
		if (isAuthenticated) {
			User authenticatedUser = userRespository.findByEmail(user.getEmail());

			if (authenticatedUser.isBanStatus()) {
				model.addAttribute("errorMsg", "Sorry, your account is banned, please contact admin, thank you...!!");
				return "login";
			}
			model.addAttribute("sessionUser", authenticatedUser);

			return "std-profile";
		} else {
			model.addAttribute("errorMsg", "Incorrect Email id or Password");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logoutService(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/login";
	}

	@GetMapping("/userProfile")
	public String openUserProfile(@SessionAttribute("sessionUser") User sessionUser) {
		return "std-profile";
	}

	@GetMapping("/myCourses")
	public String myCoursesPage(@SessionAttribute("sessionUser") User sessionUser, Model model) {

		List<Object[]> pcDList = ordersRepository.findPurchasedCourseByEmail(sessionUser.getEmail());

		List<PurchasedCourse> purchasedCourseList = new ArrayList<>();

		for (Object[] course : pcDList) {

			PurchasedCourse purchasedCourse = new PurchasedCourse();
			purchasedCourse.setPurchasedOn((String) course[0]);
			purchasedCourse.setDescription((String) course[1]);
			purchasedCourse.setImageUrl((String) course[2]);
			purchasedCourse.setCourseName((String) course[3]);
			purchasedCourse.setUpdatedOn((String) course[4]);

			purchasedCourseList.add(purchasedCourse);
		}
		model.addAttribute("purchasedCourseList", purchasedCourseList);

		return "my-courses";
	}

}
