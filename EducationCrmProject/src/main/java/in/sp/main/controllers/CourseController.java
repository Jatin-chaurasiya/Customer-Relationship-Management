package in.sp.main.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

import in.sp.main.entities.Course;
import in.sp.main.services.CourseService;

@Controller
public class CourseController {

	private String UPLOAD_DIR = "src/main/resources/static/uploads/";
	private String IMAGE_URL = "http://localhost:8080/uploads/";

	@Autowired
	private CourseService courseService;

	@GetMapping("/courseMangement")
	public String openCourseMangementPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {

		PageRequest pageable = PageRequest.of(page, size);

		Page<Course> coursePage = courseService.getCourseDetailsByPagination(pageable);

		model.addAttribute("coursePage", coursePage);

		return "Course-Mangement-page";
	}

//    -------Add course Start----------------
	@GetMapping("/addCourse")
	public String openAddCoursePage(Model model) {
		model.addAttribute("course", new Course());
		return "add-course";
	}

	@PostMapping("/addCourseForm")
	public String openAddCoursePage(@ModelAttribute("course") Course course,
			@RequestParam("courseImg") MultipartFile courseImg, Model model) {

		try {
			courseService.addCourse(course, courseImg);
			model.addAttribute("successMsg", "Course added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Course not added due to some error");
		}
		return "add-course";
	}
//  -------Add course End----------------

//	-----------Edit Course start----
	@GetMapping("/editCourse")
	public String openEditCoursePage(@RequestParam("courseName") String courseName, Model model) {

		Course course = courseService.getCourseDetails(courseName);

		model.addAttribute("course", course);
		model.addAttribute("newCourseObj", new Course());

		return "edited-course";
	}

	@PostMapping("updateCourseDetailsForm")
	public String UpdateCourseDetailsForm(@ModelAttribute("newCourseObj") Course newCourseObj,
			@RequestParam("courseImg") MultipartFile courseImg, RedirectAttributes redirectAttributes) {

		try {
			Course oldCourse = courseService.getCourseDetails(newCourseObj.getName());
			newCourseObj.setId(oldCourse.getId());
			if (!courseImg.isEmpty()) {
				String imgName = courseImg.getOriginalFilename();
				Path imgPath = Paths.get(UPLOAD_DIR + imgName);
				Files.write(imgPath, courseImg.getBytes());

				String imgUrl = IMAGE_URL + imgName;
				newCourseObj.setImageUrl(imgUrl);

			} else {
				newCourseObj.setImageUrl(oldCourse.getImageUrl());
			}

			courseService.updateCourseDetails(newCourseObj);
			redirectAttributes.addFlashAttribute("successMsg", "Course Details updated successfully..");

		} catch (Exception e) {

			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMsg", "Course Detail not updated due some error..");

		}

		return "redirect:/courseMangement";
	}
//	------Edit course end----------

//	--------delete course start-----------
	@GetMapping("/deleteCourseDetails")
	public String deleteCourseDetailPage(@RequestParam("courseName") String courseName,
			RedirectAttributes redirectAttributes) {
		try {
			courseService.deleteCourseDetails(courseName);
			redirectAttributes.addFlashAttribute("successMsg", "Course  deleted successfully..");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMsg", "Course does not deleted due some error..");
			e.printStackTrace();
		}
		return "redirect:/courseMangement";
	}

}
