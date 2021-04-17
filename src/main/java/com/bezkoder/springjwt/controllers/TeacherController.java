package com.bezkoder.springjwt.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.CourseStudentDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.Teacher;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AnnouncementRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.TeacherService;
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
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private TeacherService teacherService;

	// get all courses belong to a particular teacher
	@GetMapping("/manageCourses")
	@Cacheable(value = "courseCache")
	public ResponseEntity<?> manageCourses() {
		Teacher teacher = teacherService.findTeacherByUser(userService.getCurrentLoggedUser());
		List<CourseDto> courseDtos = courseService.coursesToCourseDtos(teacher.getCourses());
		return ResponseEntity.ok(courseDtos);
	}

	// Send any important information to all students
	@PostMapping("/sendAnnounement")
	public ResponseEntity<?> sendAnnounement(@RequestBody AnnouncementRequest announcementRequest) {
		User user = userService.getCurrentLoggedUser();
		List<User> users = userService.findAllUsers();
		List<String> emaiList = users.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		String[] receiptAdresses = emaiList.toArray(new String[emaiList.size()]);

		String content = announcementRequest.getContent();
		eventPublisher.publishEvent(new OnAnnouncementEvent(user, receiptAdresses, content));

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

	// get all students belong to a particular course to be rendered in the page
	@GetMapping("/studentInfo/{regId}")
	public ResponseEntity<?> sendAnnounement(@PathVariable Integer regId) {
		List<CourseStudentDto> courseStudentDtos = courseService.courseToCourseStudentDtos(regId);
		return ResponseEntity.ok(courseStudentDtos);
	}

//	static String[] addElement(String[] a, String... e) {
//	    String[] temptArray  = new String [a.length + e.length];
//	    System.arraycopy(a, 0, temptArray, 0, a.length);
//	    for (int i = 0; i < e.length; i++) {
//			temptArray[a.length + i ] = e[i];
//		}
//	    return temptArray;
//	}
}