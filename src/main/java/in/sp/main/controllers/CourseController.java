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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.sp.main.entities.Admin;
import in.sp.main.entities.Course;
import in.sp.main.services.CourseService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CourseController {

	@Autowired
	private CourseService courseService;

	@GetMapping("/courseMangement")
	public String openCourseMangementPage(Model model, HttpSession session,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			PageRequest pageable = PageRequest.of(page, size);
			Page<Course> coursePage = courseService.getCourseDetailsByPagination(pageable);

			model.addAttribute("coursePage", coursePage);
			return "Course-Mangement-page";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load courses");
			return "Course-Mangement-page";
		}
	}

	@GetMapping("/addCourse")
	public String openAddCoursePage(Model model, HttpSession session) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		model.addAttribute("course", new Course());
		return "add-course";
	}

	@PostMapping("/addCourseForm")
	public String addCourseForm(@ModelAttribute("course") Course course,
			@RequestParam("courseImg") MultipartFile courseImg, HttpSession session, Model model) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			if (course.getName() == null || course.getName().trim().isEmpty()) {
				model.addAttribute("errorMsg", "Course name is required");
				model.addAttribute("course", course);
				return "add-course";
			}

			courseService.addCourse(course, courseImg);
			model.addAttribute("successMsg", "Course added successfully");
			model.addAttribute("course", new Course()); // Fresh form
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMsg", e.getMessage());
			model.addAttribute("course", course);
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to add course: " + e.getMessage());
			model.addAttribute("course", course);
		}
		return "add-course";
	}

	@GetMapping("/editCourse")
	public String openEditCoursePage(@RequestParam("courseName") String courseName, HttpSession session, Model model) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			Course course = courseService.getCourseDetails(courseName);

			if (course == null) {
				model.addAttribute("errorMsg", "Course not found");
				return "redirect:/courseMangement";
			}

			model.addAttribute("course", course);
			model.addAttribute("newCourseObj", new Course());
			return "edited-course";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load course");
			return "redirect:/courseMangement";
		}
	}

	@PostMapping("/updateCourseDetailsForm")
	public String updateCourseDetailsForm(@ModelAttribute("newCourseObj") Course newCourseObj,
			@RequestParam("courseImg") MultipartFile courseImg, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			Course oldCourse = courseService.getCourseDetails(newCourseObj.getName());

			if (oldCourse == null) {
				redirectAttributes.addFlashAttribute("errorMsg", "Course not found");
				return "redirect:/courseMangement";
			}

			newCourseObj.setId(oldCourse.getId());

			if (courseImg != null && !courseImg.isEmpty()) {
				courseService.addCourse(newCourseObj, courseImg);
			} else {
				newCourseObj.setImageUrl(oldCourse.getImageUrl());
				courseService.updateCourseDetails(newCourseObj);
			}

			redirectAttributes.addFlashAttribute("successMsg", "Course updated successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to update course: " + e.getMessage());
		}

		return "redirect:/courseMangement";
	}

	@PostMapping("/deleteCourseDetails")
	public String deleteCourseDetailPage(@RequestParam("courseName") String courseName, HttpSession session,
			RedirectAttributes redirectAttributes) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			courseService.deleteCourseDetails(courseName);
			redirectAttributes.addFlashAttribute("successMsg", "Course deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to delete course: " + e.getMessage());
		}
		return "redirect:/courseMangement";
	}
}