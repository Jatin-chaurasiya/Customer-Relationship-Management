package in.sp.main.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.sp.main.entities.Course;
import in.sp.main.repositories.CourseRepository;

@Service
public class CourseService {

	private String UPLOAD_DIR = "src/main/resources/static/uploads/";
	private String IMAGE_URL = "http://localhost:8080/uploads/";

	@Autowired
	private CourseRepository courseRepository;

	public List<Course> getAllCourseDetails() {
		return courseRepository.findAll();
	}

	public void addCourse(Course course, MultipartFile courseImg) throws IOException {
		String imgName = courseImg.getOriginalFilename();
		Path imgPath = Paths.get(UPLOAD_DIR + imgName);
		Files.write(imgPath, courseImg.getBytes());

		String imgUrl = IMAGE_URL + imgName;
		course.setImageUrl(imgUrl);
		courseRepository.save(course);
	}

	public Course getCourseDetails(String courseName) {

		return courseRepository.findByName(courseName);
	}

	public Page<Course> getCourseDetailsByPagination(Pageable pageable) {

		return courseRepository.findAll(pageable);
	}

	public void updateCourseDetails(Course course) {
		courseRepository.save(course);
	}

	public void deleteCourseDetails(String courseName) {

		Course course = courseRepository.findByName(courseName);
		if (course != null) {
			courseRepository.delete(course);
		} else {
			throw new RuntimeException("Course not found with name : " + courseName);
		}
	}

	public List<String> getAllCourseName() {
		List<Course> courseList = courseRepository.findAll();

		List<String> courseNameList = new ArrayList<>();
		for (Course course : courseList) {
			String CourseName = course.getName();
			courseNameList.add(CourseName);
		}
		return courseNameList;
	}

}
