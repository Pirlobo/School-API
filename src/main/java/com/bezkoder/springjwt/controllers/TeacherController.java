package com.bezkoder.springjwt.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.CourseStudentDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AnnouncementRequest;
import com.bezkoder.springjwt.payload.request.RegisterRequest;
import com.bezkoder.springjwt.payload.request.ResetPasswordRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;
import com.bezkoder.springjwt.security.service.CalendarService;
import com.bezkoder.springjwt.security.service.CourseService;
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
	
	@GetMapping("/manageCourses")
	@Cacheable(value = "courseCache")
	public ResponseEntity<?> manageCourses() {
		List<CourseDto> courseDtos = courseService.coursesToCourseDtos(courseService.findAll()); 
		return ResponseEntity.ok(courseDtos);
	}
	
	@PostMapping("/sendAnnounement")
	public ResponseEntity<?> sendAnnounement(@RequestBody AnnouncementRequest announcementRequest) {
		User user = userService.findByUsername(announcementRequest.getUserName());
		List<User> users = userService.findAllUsers();
		List<String> emaiList  = users.stream().map(u -> u.getEmail()).collect(Collectors.toList());
		String[] receiptAdresses = emaiList.toArray(new String[emaiList.size()]);

		String content = announcementRequest.getContent(); 
		eventPublisher.publishEvent(new OnAnnouncementEvent(user, receiptAdresses, content));
		
		return ResponseEntity.ok(new MessageResponse("Sent"));
	}
	@GetMapping("/studentInfo/{regId}")
	public ResponseEntity<?> sendAnnounement(@PathVariable Integer regId) {
		List<CourseStudentDto> courseStudentDtos = courseService.courseToCourseStudentDtos(regId);
		return ResponseEntity.ok(courseStudentDtos);
	}
	
	static String[] addElement(String[] a, String... e) {
	    String[] temptArray  = new String [a.length + e.length];
	    System.arraycopy(a, 0, temptArray, 0, a.length);
	    for (int i = 0; i < e.length; i++) {
			temptArray[a.length + i ] = e[i];
		}
	    return temptArray;
	}
	

}