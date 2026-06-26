package in.sp.main.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.sp.main.entities.Course;
import in.sp.main.repositories.CourseRepository;

@Service
public class CourseService {

	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
	private static final String IMAGE_URL = "/uploads/";
	private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
	private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

	@Autowired
	private CourseRepository courseRepository;

	public List<Course> getAllCourseDetails() {
		return courseRepository.findAll();
	}

	public void addCourse(Course course, MultipartFile courseImg) throws IOException {

		if (course == null || course.getName() == null || course.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Course name is required");
		}

		if (courseRepository.existsByName(course.getName().trim())) {
			throw new IllegalArgumentException("Course already exists with this name");
		}

		createUploadDirectoryIfNotExists();

		if (courseImg != null && !courseImg.isEmpty()) {
			String imgUrl = saveImage(courseImg);
			course.setImageUrl(imgUrl);
		} else {
			course.setImageUrl("/uploads/default-course.jpg");
		}

		courseRepository.save(course);
	}

	public Course getCourseDetails(String courseName) {
		if (courseName == null || courseName.trim().isEmpty()) {
			return null;
		}
		return courseRepository.findByName(courseName.trim());
	}

	public Page<Course> getCourseDetailsByPagination(Pageable pageable) {
		return courseRepository.findAll(pageable);
	}

	public void updateCourseDetails(Course course) {
		if (course == null || course.getId() == null) {
			throw new IllegalArgumentException("Invalid course data");
		}
		courseRepository.save(course);
	}

	public void deleteCourseDetails(String courseName) {
		if (courseName == null || courseName.trim().isEmpty()) {
			throw new IllegalArgumentException("Course name is required");
		}

		Course course = courseRepository.findByName(courseName.trim());
		if (course != null) {
			courseRepository.delete(course);
		} else {
			throw new RuntimeException("Course not found with name: " + courseName);
		}
	}

	public List<String> getAllCourseName() {
		List<Course> courseList = courseRepository.findAll();
		List<String> courseNameList = new ArrayList<>();

		for (Course course : courseList) {
			if (course.getName() != null) {
				courseNameList.add(course.getName());
			}
		}
		return courseNameList;

	}

	private String saveImage(MultipartFile file) throws IOException {

		if (file.getSize() > MAX_FILE_SIZE) {
			throw new IOException("File size exceeds maximum limit of 5MB");
		}

		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.isEmpty()) {
			throw new IOException("Invalid file name");
		}

		String extension = getFileExtension(originalFilename);
		if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
			throw new IOException("Only JPG, JPEG, PNG, GIF files are allowed");
		}

		String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
		Path uploadPath = Paths.get(UPLOAD_DIR + uniqueFilename);

		Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

		return IMAGE_URL + uniqueFilename;
	}

	private String getFileExtension(String filename) {
		int lastDotIndex = filename.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
			return filename.substring(lastDotIndex + 1);
		}
		return "";
	}

	private void createUploadDirectoryIfNotExists() {
		File uploadDir = new File(UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
	}
}