package com.bezkoder.springjwt.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.CourseStudentDto;
import com.bezkoder.springjwt.dto.GradeDto;
import com.bezkoder.springjwt.exceptions.BookExistsException;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.Grade;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.Teacher;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AddBookRequest;
import com.bezkoder.springjwt.payload.request.AnnouncementRequest;
import com.bezkoder.springjwt.payload.request.GradeRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;
import com.bezkoder.springjwt.security.service.BookService;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.TeacherService;
import com.bezkoder.springjwt.security.service.UserCourseService;
import com.bezkoder.springjwt.security.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
	@Autowired
	private UserService userService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("/grade")
	public ResponseEntity<?> grade(@RequestBody GradeRequest gradeRequest) {
		teacherService.grade(gradeRequest);
		return ResponseEntity.ok(new MessageResponse("Graded"));
	}

	// get all current courses belong to a particular teacher
	@GetMapping("/manageCourses")
	@Cacheable(value = "courseCache")
	public ResponseEntity<?> manageCourses() {
		int year = Calendar.getInstance().get(Calendar.YEAR) + 1;
		Teacher teacher = teacherService.findTeacherByUser(userService.getCurrentLoggedUser());
		List<CourseDto> convertedObjects = courseService.coursesToCourseDtos(teacher.getCourses());  
		List<CourseDto> courseDtos = convertedObjects.stream().filter(e -> e.getYear().equals(String.valueOf(year))).
                collect(Collectors.toList());
		return ResponseEntity.ok(courseDtos);
	}
	
	@GetMapping("/getAllStudentsByCourse/{regId}")
	public ResponseEntity<?> getAllStudentsByCourse(@PathVariable Integer regId) {
		List<GradeDto> gradeDtos = teacherService.getAllStudentsByCourseForGrading(regId);
		return ResponseEntity.ok(gradeDtos);
	}
	
	@GetMapping("/findStudentByUsername/{regId}")
	public ResponseEntity<?> getStudentCourseById(@PathVariable Integer regId, @RequestParam("studentName") String studentName) {
		GradeDto gradeDto = teacherService.findGradedStudentByName(regId, studentName);
		return ResponseEntity.ok(gradeDto);
	}
	// Send any important information to all students
	@PostMapping("/sendAnnounement")
	public ResponseEntity<?> sendAnnounement(@RequestBody AnnouncementRequest announcementRequest) {
		teacherService.sendAnnouncementToAllStudents(announcementRequest);
		return ResponseEntity.ok(new MessageResponse("Sent"));
	}

	// aimed to drop students who does not attend the first class
	@PostMapping("/drop")
	public ResponseEntity<?> dropClasses(@RequestBody String dropClassesRequest) {
		JSONObject json = new JSONObject(dropClassesRequest);
		JSONArray jsonArray = json.getJSONArray("userName");
		String fetchedRegId = json.getString("regId");
		List<User> users = new ArrayList<>();
		jsonArray.forEach(e -> {
			User user = userService.findByUsername(e.toString());
			users.add(user);
		});
		int id = Integer.parseInt(fetchedRegId);
		List<StudentCourse> studentCourses = teacherService.dropClasses(users, id);
		return ResponseEntity.ok(studentCourses);
	}
	
	@PostMapping("/addBook")
	public ResponseEntity<?> addBook(@RequestBody AddBookRequest addBookRequest) throws BookExistsException{
		Books book = bookService.findById(addBookRequest.getIsbn());
		if (book == null) {
			Course course = courseService.findCourseById(addBookRequest.getRegId());
			Books createdBook = new Books(addBookRequest.getIsbn(), addBookRequest.getTitle(), addBookRequest.getPublisher(), addBookRequest.getListOfAuthors(), addBookRequest.getImageUrl(), course);
			bookService.save(createdBook);
			List<Books> bookList = course.getBookList();
			return ResponseEntity.ok(bookList);
		} else {
			throw new BookExistsException("This book is already saved");
		}
		
	}

	// get all students belong to a particular course to be rendered in the page
	@GetMapping("/studentInfo/{regId}")
	public ResponseEntity<?> sendAnnounement(@PathVariable Integer regId) {
		List<CourseStudentDto> courseStudentDtos = courseService.courseToCourseStudentDtos(regId);
		return ResponseEntity.ok(courseStudentDtos);
	}

	@GetMapping("getRequiredBooksByCourse/{regId}")
	public ResponseEntity<?> getRequiredBooksByCourse(@PathVariable Integer regId){
		Course course = courseService.findCourseById(regId);
		List<Books> books = course.getBookList();
		return ResponseEntity.ok(books);
	}
	
	
}