package com.bezkoder.springjwt.controllers;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.bezkoder.springjwt.dto.TranscriptDto;
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

//	@Cacheable(value = "courseCache")
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
		// true if all taken classes have the same term. For ex, fall
		// Return true, all taken classes should have the same term to be registered
		if (courseService.isTheSameTerm(courses)) {
			if (calendarService.isInRegistrationTime(courses.get(0).getTerm().getSemester())) {
				if (!courseService.isDuplicated(reg_ids)) {
					if (faileRegisterCourses.size() == 0) {
						userService.registerForClassess(user, courses);
						List<CourseDto> courseDtos = courseService.coursesToCourseDtos(courses);
						return ResponseEntity.ok(courseDtos);
					} else {
						// 1 : Cant repeat taken class/classes
						// 2 : Prerequisites are required
						// 3 : Courses are conflicted
						// 4 :no errors occur 
						int registerStatus = courseService.registerStatus(user, courses, reg_ids);
						switch (registerStatus) {
						case 1:
							return ResponseEntity.ok(new MessageResponse("Cant repeat taken class/classes !!!"));
						case 2:
							return ResponseEntity.ok(new MessageResponse("Prerequisites are required !!!"));
						case 3:
							return ResponseEntity.ok(new MessageResponse("Courses are conflicted !!!"));
						default:
							break;
						}
						
					}
				} else {
					return ResponseEntity.ok(new MessageResponse("Courses are duplicated"));
				}
			}

			else {
				return ResponseEntity
						.ok(new MessageResponse("Can not register at this time"));
			}
		}
		// terms of taken classes are different
		else {
			return ResponseEntity.ok(new MessageResponse("All taken classes should have the same term"));
		}
		return null;

	}

	// get Current Registered Classes
	@GetMapping("/getCurrentRegisteredClasses")
	public ResponseEntity<?> getCurrentRegisteredClasses() {
		List<Course> registeredCourses = courseService.getIPCourses();
		List<CourseDto> courseDtos = courseService.coursesToCourseDtos(registeredCourses);
		return ResponseEntity.ok(courseDtos);
	}
	@GetMapping("/getIntendedDroppedCourses")
	public ResponseEntity<?> getIntendedCourses() {
		List<Course> registeredCourses = courseService.getIntendedDroppdeCourses();
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

	@GetMapping("/getCourseGrades")
	public ResponseEntity<?> getGrades() {
		List<TranscriptDto> transcriptDtos = userService.getCourseGrades();
		return ResponseEntity.ok(transcriptDtos);
	}
	
	@GetMapping("/getTranscript")
	public ResponseEntity<?> getTranscript() {
		return ResponseEntity.ok(userService.getTranscript());
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
	
	@GetMapping("getCourseDescriptionByRegId/{regId}")
	public ResponseEntity<?> getCourseDescriptionByRegId(@PathVariable Integer regId){
		Course course = courseService.findCourseById(regId);
		String courseDescription = course.getSubject().getDescription();
		return ResponseEntity.ok(new MessageResponse(courseDescription));
	}

}

//if (calendarService.isInRegistrationTime(courses.get(0).getTerm().getSemester())) {} 
//// terms of taken classes are different
//else {
//	return ResponseEntity.ok(new MessageResponse("All taken classes should have the same term"));
//}