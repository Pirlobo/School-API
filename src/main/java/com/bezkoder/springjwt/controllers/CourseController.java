package com.bezkoder.springjwt.controllers;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RegisterRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.PaginationResponse;
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

	@Autowired
	UserService userService;

	@Autowired
	private EntityManager em;

	@Autowired
	private PaginationService paginationService;

	@Cacheable(value = "courseCache")
	@RequestMapping("/searchCoursesByTeacherOrTitle")
	public ResponseEntity<?> getCourseByTeacherOrTitle(@RequestParam("search") String search) {
		return findPaginated(1, search, "reg_id", "asc");
	}

	@GetMapping("/page/{pageNo}")
	public ResponseEntity<?> findPaginated(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("search") String search, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir) {
		try {
//	PaginationResponse paginationResponse = paginationService.getPagination(pageNo, title, sortField, sortDir);
//	List<CourseDto> courseList = paginationResponse.getDtoCourses();
			PaginationResponse paginationResponse = paginationService.getPagination(pageNo, search, sortField, sortDir);
			return ResponseEntity.ok(paginationResponse);
		} catch (Exception e) {
			throw new ResourceNotFoundException("URL does not match");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerForClasses(@RequestBody RegisterRequest regIdClasses) throws InterruptedException {
		JSONObject json = new JSONObject(regIdClasses);
		JSONArray jsonArray = json.getJSONArray("regIdClasses");
		List<Integer> reg_ids = courseService.convertJsonArrayCoursesToRegIds(jsonArray);
		List<Course> courses = courseService.convertJsonToCourses(jsonArray);
		User user = userService.getCurrentLoggedUser();
		Set<Course> faileRegisterCourses = courseService.getFailedRegisteredClasses(user, courses, reg_ids);
		if (calendarService.isBeforeDefaultTime()) {
			if (!courseService.isDuplicated(reg_ids)) {
				if (faileRegisterCourses.size() == 0) {
					userService.registerForClassess(user, courses);
					List<CourseDto> courseDtos = courseService.coursesToCourseDtos(courses);
					return ResponseEntity.ok(courseDtos);
				} else {
					return ResponseEntity.ok(new MessageResponse("Error !!!"));
				}
			} else {
				return ResponseEntity.ok(new MessageResponse("Courses are duplicated"));
			}
		}

		else {
			return ResponseEntity.ok(new MessageResponse("Semester already began"));
		}

	}

	// get Current Registered Classes
	@GetMapping("/getCurrentRegisteredClasses")
	public ResponseEntity<?> getCurrentRegisteredClasses() {
		List<Course> registeredCourses = courseService.getIPCourses();
		List<CourseDto> courseDtos = courseService.coursesToCourseDtos(registeredCourses);
		return ResponseEntity.ok(courseDtos);
	}

	@GetMapping("/getAllRegisteredClasses")
	public ResponseEntity<?> getAllRegisteredClasses() {
		User user = userService.getCurrentLoggedUser();
		List<Course> registeredCourses = userService.getYourClasses(user);
		List<CourseDto> courseDtos = courseService.coursesToCourseDtos(registeredCourses);
		return ResponseEntity.ok(courseDtos);
	}

	@PostMapping("/dropClasses")
	public ResponseEntity<?> dropClasses(@RequestBody RegisterRequest regIdClasses) {
		JSONObject json = new JSONObject(regIdClasses);
		JSONArray jsonArray = json.getJSONArray("regIdClasses");
		List<Integer> reg_ids = courseService.convertJsonArrayCoursesToRegIds(jsonArray);
		User user = userService.getCurrentLoggedUser();
		List<CourseDto> droppedClasses = userService.dropClasses(user, reg_ids);
		return ResponseEntity.ok(droppedClasses);
	}

	// how to configure stored procedure if needed
	// for learning purpose only
	@GetMapping("/test")
	public int test(Integer year, String title) {
		year = 2022;
		title = "COMSC";
		StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("test")
				.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
				.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT).setParameter(1, year)
				.setParameter(2, title);
		return (int) storedProcedure.getOutputParameterValue(3);
	}
}