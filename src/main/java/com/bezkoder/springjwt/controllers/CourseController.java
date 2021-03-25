package com.bezkoder.springjwt.controllers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RegisterRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;
import com.bezkoder.springjwt.security.service.CalendarService;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.UserService;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CalendarService calendarService;
	
	@Autowired UserService userService;
	

	@RequestMapping("/searchCoursesByTitle/{title}")
//	@CacheEvict(value = "courseCache", key = "#result.title", beforeInvocation = true)
//	@Cacheable(value = "courseCache") 
	public ResponseEntity<?> getCourseByTitle(@PathVariable String title) {
		List<CourseDto> courseList = courseService.searchCourses(title);
		if (courseList.size() == 0) {
			return new ResponseEntity(new MessageResponse("Not Found"), HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity.ok(courseList);
		}
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerForClasses(@RequestBody String userName, RegisterRequest regIdClasses) throws InterruptedException {
		JSONObject json = new JSONObject(userName);
		JSONArray jsonArray = json.getJSONArray("regIdClasses");
		
		String fetchUserName = json.getString("userName");
		
		List<Integer> reg_ids =  courseService.convertJsonArrayCoursesToRegIds(jsonArray);
		List<Course> courses = courseService.convertJsonToCourses(jsonArray);
	    User user = userService.findByUsername(fetchUserName);
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
	    User user = userService.findByUsername(fetchUserName);
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
	    User user = userService.findByUsername(fetchUserName);
		List<CourseDto> droppedClasses = userService.dropClasses(user, reg_ids);
		return ResponseEntity.ok(droppedClasses);
	}
	
	

}