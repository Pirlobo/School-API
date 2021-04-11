package com.bezkoder.springjwt.controllers;

import java.util.Calendar;
import java.util.List;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.http.HttpServletRequest;
import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RegisterRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.PaginationResponse;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;
import com.bezkoder.springjwt.security.service.CalendarService;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.PaginationService;
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
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private PaginationService paginationService;

//	@RequestMapping("/searchCoursesByTitle")
////	@CacheEvict(value = "courseCache", key = "#result.title", beforeInvocation = true)
////	@Cacheable(value = "courseCache") 
//	public ResponseEntity<?> getCourseByTitle(@RequestParam("title") String title) {
//		List<CourseDto> courseList = courseService.searchCourses(title);
//		if (courseList.size() == 0) {
//			return new ResponseEntity(new MessageResponse("Not Found"), HttpStatus.NOT_FOUND);
//		} else {
//			System.out.println(courseList.size());
//			return ResponseEntity.ok(courseList);
//		}
//		
//	}
	@RequestMapping("/searchCoursesByTitle")
	public ResponseEntity<?> getCourseByTitle(@RequestParam("title") String title) {
		System.out.println(title);
		return findPaginated(1, title, "reg_id", "asc");
	}
	
	
	@GetMapping("/page/{pageNo}")
	public ResponseEntity<?> findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("title") String title,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir) {
		System.out.println(sortField);
//		PaginationResponse paginationResponse = paginationService.getPagination(pageNo, title, sortField, sortDir);
//		List<CourseDto> courseList = paginationResponse.getDtoCourses();
		PaginationResponse paginationResponse = paginationService.getPagination(pageNo, title, sortField, sortDir);
		return ResponseEntity.ok(paginationResponse);
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
	
	@GetMapping("/getRegisteredClasses")
	public ResponseEntity<?> getRegisteredClasses() {
	    User user = userService.getCurrentLoggedUser();
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
	    System.out.println(user.getEmail());
	    System.out.println(reg_ids.size());
		List<CourseDto> droppedClasses = userService.dropClasses(user, reg_ids);
		return ResponseEntity.ok(droppedClasses);
	}
	
//	@SuppressWarnings("uncheck")
	// a function created to test for stored procedure
	// for learning purpose only
	@GetMapping("/test")
	public int test (Integer year , String title) {
		year = 2021;
		title = "COMSC";
		StoredProcedureQuery storedProcedure = 
		          em
		            .createStoredProcedureQuery("test")
		            .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
		            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
		            .registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT)
		            .setParameter(1, year)
		            .setParameter(2, title);
		return (int) storedProcedure.getOutputParameterValue(3);
	}
}