package com.bezkoder.springjwt.controllers;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentDto;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RegisterRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.UserRepository;
import com.bezkoder.springjwt.security.service.CalendarService;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.UserService;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CalendarService calendarService;
	
	@Autowired UserService userService;
	
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	

	@RequestMapping("/searchCoursesByTitle/{title}")
	public ResponseEntity<?> getCourseByTitle(@PathVariable String title, HttpServletRequest request) {
		List<CourseDto> courseList = courseService.searchCourses(title);
		if (courseList.size() == 0) {
			return new ResponseEntity(new MessageResponse("Not Found"), HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity.ok(courseList);
		}
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerForClasses(@RequestBody String userName, RegisterRequest regIdClasses) {
		JSONObject json = new JSONObject(userName);
		JSONArray jsonArray = json.getJSONArray("regIdClasses");
		
		String fetchUserName = json.getString("userName"); 
		
		List<Integer> reg_ids =  courseService.convertJsonArrayCoursesToRegIds(jsonArray);
		List<Course> courses = courseService.convertJsonToCourses(jsonArray);
	    User user = userRepository.findByUsername(fetchUserName).orElse(null);
		Set<Course> faileRegisterCourses = courseService.getFailedRegisteredClasses(user, courses, reg_ids)  ;
		if (calendarService.isBeforeDefaultTime()) {
			if(!courseService.isDuplicated(reg_ids)) {
				if (faileRegisterCourses.size() == 0) {
					userService.registerForClassess(user, courses);
					List<CourseDto> courseDtos = courseService.coursesToCourseDtos(courses);
					return ResponseEntity.ok(courseDtos);
				} else {
					return ResponseEntity.ok(new MessageResponse("Error !!!"));
				}
			}
			else {
				return ResponseEntity.ok(new MessageResponse("Courses are duplicated"));
			}
		}

		else {
			return ResponseEntity.ok(new MessageResponse("Semester already began"));
		}

	}
	
	@PostMapping("/getRegisteredClasses")
	public ResponseEntity<?> getRegisteredClasses(@RequestBody String userName) {
		JSONObject json = new JSONObject(userName);
		String fetchUserName = json.getString("userName");
	    User user = userRepository.findByUsername(fetchUserName).orElse(null);
		List<Course> registeredCourses = userService.getYourClasses(user);
		List<CourseDto> courseDtos = courseService.coursesToCourseDtos(registeredCourses);
		return ResponseEntity.ok(courseDtos);
	}
	@PostMapping("/dropClasses")
	public ResponseEntity<?> dropClasses(@RequestBody String userName, RegisterRequest regIdClasses) {
		JSONObject json = new JSONObject(userName);
		JSONArray jsonArray = json.getJSONArray("regIdClasses");
		String fetchUserName = json.getString("userName"); 
		List<Integer> reg_ids =  courseService.convertJsonArrayCoursesToRegIds(jsonArray);
	    User user = userRepository.findByUsername(fetchUserName).orElse(null);
		List<CourseDto> droppedClasses = userService.dropClasses(user, reg_ids);
		return ResponseEntity.ok(droppedClasses);
	}
	
	

}
