package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.noneDSA;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IStandaloneElementTag;

import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.CourseStudentDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RegisterRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.persistence.BookRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;
import com.bezkoder.springjwt.persistence.UserRepository;
import com.google.common.collect.Lists;

import antlr.debug.NewLineEvent;

@Service
@CacheConfig(cacheNames={"course"})   
public class CourseService implements ICourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	private StudentCourseRepository studentCourseRepository;
	
	@Autowired
	private UserCourseService userCourseService;

	@Override
	public Course findCourseById(Integer id) {
		Course course = courseRepository.findById(id).orElse(null);
		return course;
	}

	@Override
	public List<CourseDto> searchCourses(String title) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<Course> courseList = courseRepository.findByTitle(year, title);
		List<CourseDto> resultSet = new ArrayList<>();
		courseList.forEach(e -> {
			String from = e.getCalendars().get(0).getStartTime().toString().substring(0, 5);
			String to = e.getCalendars().get(0).getEndTime().toString().substring(0, 5);
			String prerequisite;
			try {
				prerequisite = e.getSubject().getPrerequisite().getSubjectCode().toString();
			} catch (Exception e2) {
				prerequisite = "None";
			}
			
			
			CourseDto courseDto = new CourseDto(e.getRegId(), e.getSection(), e.getAvailable(), e.getCapacity(),
					e.getStartDay(), e.getEndDay(), e.getWaitlist(), e.getSubject().getSubjectCode().toString(),
					e.getRoom().getRoomName(), e.getTeacher().getName(), from, to, prerequisite,
					e.getTerm().getSemester().toString(), e.getSubject().getDescription(), null);
			resultSet.add(courseDto);
		});
		return resultSet;

	}

	@Override
	// @Cacheable(value = "courseCache")
	public Set<Books> getAllRequiredBooks(String userName) {
		Set<Books> bookList = new HashSet<Books>();
		User user = userRepository.findByUsername(userName).orElse(null);
		List<Course> courses = userService.getYourClasses(user);
		courses.forEach(e -> {
			List<Books> books = e.getBooks();
			bookList.addAll(books);
		});
		return bookList;
	}

	@Override
	// @Cacheable(value = "courseCache")
	public List<Course> findRegisteredClasses(List<Integer> regIdClasses) {
		List<Course> checkedCourses = new ArrayList<Course>();
		for (Integer integer : regIdClasses) {
			Course checkCourse = courseRepository.findById(integer).orElse(null);
			checkedCourses.add(checkCourse);
		}
		return checkedCourses;
	}

	@Override
	public boolean isAlaivable(Course course) {
		if (course.getAvailable() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void save(Course course) {
		courseRepository.save(course);

	}

	@Override
	public Set<Course> getFailedRegisteredClasses(User user, List<Course> courses, List<Integer> regIdClasses) {
		Set<Course> failedRegisteredClasses = new HashSet<Course>();
		List<Course> registeredCourses = userService.getYourClasses(user);
		List<Course> courses2 = findRegisteredClasses(regIdClasses);
		courses2.forEach(e -> {
			
			if (!(isPreregquisited(user, e))) {
				System.out.println("a");
				failedRegisteredClasses.add(e);
			} else {
				System.out.println("b");
				List<Course> currentCourses = userService.getYourClasses(user);
				for (int i = 0; i < currentCourses.size(); i++) {
					if (currentCourses.get(i).getTerm().getSemester().toString()
							.equals(e.getTerm().getSemester().toString())) {
						if (currentCourses.get(i).getTerm().getYear() == e.getTerm().getYear()) {
							System.out.println("d");
							failedRegisteredClasses.add(e);
						}
					}

				}

			}
			if (!isNotTimeConflicted(user, e, regIdClasses)) {
				System.out.println("c");
				failedRegisteredClasses.add(e);
			}
		});

		for (int i = 0; i < regIdClasses.size(); i++) {
			Course course1 = courseRepository.findById(regIdClasses.get(i)).orElse(null);
			if (!(isRepeated(user, course1))) {
				System.out.println("f");
				failedRegisteredClasses.add(course1);
			}

		for (int j = 0; j < registeredCourses.size(); j++) {
				if (regIdClasses.get(i) == registeredCourses.get(j).getRegId()) {
					System.out.println("e");
					Course course = courseRepository.findById(regIdClasses.get(i)).orElse(null);
					failedRegisteredClasses.add(course);

				}
			}
		}
		return failedRegisteredClasses;
	}

	// get successfullRegisteredClasses by extracting from the method
	// getFailedRegisteredClasses()
	@Override
	public List<Course> getSuccessfullRegisteredClasses(User user, List<Course> courses, List<Integer> regIdClasses) {
		List<Course> registeredCourses = userService.getYourClasses(user);
		Set<Course> failedRegisteredCourses = getFailedRegisteredClasses(user, registeredCourses, regIdClasses);
		List<Course> successfullRegisteredClasses = findRegisteredClasses(regIdClasses);

		List<Course> a = new ArrayList<>(failedRegisteredCourses);
		for (int i = 0; i < failedRegisteredCourses.size(); i++) {
			for (int j = 0; j < successfullRegisteredClasses.size(); j++) {
				if (a.get(i).getRegId() == successfullRegisteredClasses.get(j).getRegId()) {
					successfullRegisteredClasses.remove(j);
				}
			}
		}

		return successfullRegisteredClasses;
	}

	@Override
	public void setAvailable(Integer available, Integer id) {
		courseRepository.setAvailable(available, id);

	}

	@Override
	public void setWailist(Integer waitlist, Integer id) {
		courseRepository.setWailist(waitlist, id);

	}

	@Override
	public List<Course> findBySubject(String subjectName) {
		return courseRepository.findByArea(subjectName);

	}

	// must be true to register for for course which has prerequisite course
	@Override
	public boolean isPreregquisited(User user, Course course) {
		List<Course> registeredCourses = userService.getYourClasses(user);

		if (course.getSubject().getPrerequisite() != null) {

			for (int i = 0; i < registeredCourses.size(); i++) {
				StudentCourse userCourse = new StudentCourse(user, registeredCourses.get(i));
				if (registeredCourses.get(i).getSubject().getSubjectCode() == course.getSubject().getPrerequisite()
						.getSubjectCode() && userCourse.getIsPassed() == IsPassed.TRUE) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isPassed(User user, Course course) {
		StudentCourse userCourse = new StudentCourse(user, course);
		if (userCourse.getIsPassed() == (IsPassed.FALSE)) {
			return false;
		} else if (userCourse.getIsPassed() == (IsPassed.TRUE)) {
			return true;
		}
		return true;
	}

	// cho repeat nếu true, otherwise false
	@Override
	public boolean isRepeated(User user, Course course) {
		List<Course> currentCourses = userService.getYourClasses(user);
		for (int i = 0; i < currentCourses.size(); i++) {
			if (currentCourses.get(i).getSubject().getSubjectCode().toString().equals(course.getSubject().getSubjectCode().toString())) {
				
				if (!(isPassed(user, currentCourses.get(i)))) {
					if (currentCourses.get(i).getTerm().getSemester().toString()
							.equals(course.getTerm().getSemester().toString())) {
						if (currentCourses.get(i).getTerm().getYear() == course.getTerm().getYear()) {
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
					
				}
				return false;
			}
			

		}
		return true;
	}

	// register mutiple courses with the same subject_code at the same time
	@Override
	public boolean isDuplicated(List<Integer> regIdClasses) {
		List<Course> checkedCourses = findRegisteredClasses(regIdClasses);
		System.out.println(checkedCourses.get(0).getRegId());
		for (int i = 0; i < checkedCourses.size(); i++) {

			for (int j = i + 1; j < checkedCourses.size(); j++) {
				if (checkedCourses.get(i).getSubject().getSubjectCode().toString()
						.equals(checkedCourses.get(j).getSubject().getSubjectCode().toString())) {
					return true;
				}

			}

		}
		return false;
	}

	@Override
	public List<Course> getFilterdUnDuplicatedCourses(User user, List<Integer> regIdClasses) {
		List<Course> unDuplicatedCourses = findRegisteredClasses(regIdClasses);
		System.out.println(unDuplicatedCourses.size());
		List<Course> removedCourses = findRegisteredClasses(regIdClasses);
		for (int i = 0; i < removedCourses.size(); i++) {

			for (int j = i + 1; j < removedCourses.size(); j++) {
				if (removedCourses.get(i).getSubject().getSubjectCode().toString()
						.equals(removedCourses.get(j).getSubject().getSubjectCode().toString())) {

					Course removedCourse = removedCourses.get(i);

					removedCourses.forEach(e -> {
						if (e.getSubject().getSubjectCode() == removedCourse.getSubject().getSubjectCode()) {
							unDuplicatedCourses.remove(e);
						}
					});

				}

			}

		}
		List<Course> registeredCourses = userService.getYourClasses(user);

		List<Integer> regId = new ArrayList<Integer>();
		unDuplicatedCourses.forEach(e -> {
			regId.add(e.getRegId());
		});

		List<Course> successfullRegisteredCourses = getSuccessfullRegisteredClasses(userService.getCurrentLoggedUser(),
				registeredCourses, regId);
		return successfullRegisteredCourses;

	}

	// return true if course is not conflicted
	@Override
	public boolean isNotTimeConflicted(User user, Course course, List<Integer> regIdClasses) {
		List<Course> courses = findRegisteredClasses(regIdClasses);
		List<Course> registeredCourses = userService.getYourClasses(user);
		registeredCourses.addAll(courses);
		for (int i = 0; i < registeredCourses.size(); i++) {
			if (course.getRegId() != registeredCourses.get(i).getRegId()) {
				if (course.getCalendars().get(0).getStartTime()
						.before(registeredCourses.get(i).getCalendars().get(0).getStartTime())
						&& course.getCalendars().get(0).getEndTime()
								.before(registeredCourses.get(i).getCalendars().get(0).getStartTime())) {

					continue;
				} else if (course.getCalendars().get(0).getStartTime()
						.after(registeredCourses.get(i).getCalendars().get(0).getEndTime())
						&& course.getCalendars().get(0).getEndTime()
								.after(registeredCourses.get(i).getCalendars().get(0).getEndTime())) {
					continue;
				} else {

					return false;
				}
			}

			else {
				continue;
			}

		}
		return true;

	}

	public boolean isCourseConflicted(User user, List<Integer> regIdClasses) {
		int a = 0;
		List<Course> unConflictedCourses = new ArrayList<Course>();
		List<Course> registeredClasses = courseService.findRegisteredClasses(regIdClasses);
		for (int i = 0; i < registeredClasses.size(); i++) {
			if (courseService.isNotTimeConflicted(user, registeredClasses.get(i), regIdClasses)) {
				unConflictedCourses.add(registeredClasses.get(i));
			} else {

				a++;

			}
		}
		if (a == 0) {
			return false;
		} else {
			// redirectAttributes.addFlashAttribute("error", "C");
			return true;
		}
	}

	public List<Course> getUnconflictedCourse(User user, List<Integer> regIdClasses) {
		int a = 0;
		List<Course> unConflictedCourses = new ArrayList<Course>();
		List<Course> registeredClasses = courseService.findRegisteredClasses(regIdClasses);
		for (int i = 0; i < registeredClasses.size(); i++) {
			if (courseService.isNotTimeConflicted(user, registeredClasses.get(i), regIdClasses)) {
				unConflictedCourses.add(registeredClasses.get(i));

			}
		}
		return unConflictedCourses;
	}

	// 1 -> getUnconflictedCourses are all registered
	// 2 -> getUnconflictedCourses require pre course
	// 3-> One or more of the selected courses is already registered
	// 4 -> getUnconflictedCourses.size() = 0
	public Integer checkRegisteredCourses(User user, List<Integer> regIdClasses) {
		int a = 0;
		List<Course> registeredClasses = getUnconflictedCourse(user, regIdClasses);
		if (registeredClasses.size() == 0) {
			return 4;
		}
		System.out.println(registeredClasses.size());
		List<Course> courses = userService.getYourClasses(user);
		if ((courseService.getFailedRegisteredClasses(userService.getCurrentLoggedUser(), courses, regIdClasses))
				.size() == 0) {

			userService.registerForClassess(userService.getCurrentLoggedUser(), registeredClasses);
			return 1;
		} else {
			for (int i = 0; i < registeredClasses.size(); i++) {
				if (!(courseService.isPreregquisited(user, registeredClasses.get(i)))) {
					a++;
				} else {
					List<Course> c = new ArrayList<Course>();
					c.add(registeredClasses.get(i));
					userService.registerForClassess(userService.getCurrentLoggedUser(), c);

				}

			}
			if (a > 0) {

				return 2;
			}
			List<Course> courses2 = courseService.getSuccessfullRegisteredClasses(userService.getCurrentLoggedUser(),
					courses, regIdClasses);
			userService.registerForClassess(userService.getCurrentLoggedUser(), courses2);

			return 3;
		}
	}

	public boolean isAnyCourseSelected(List<Integer> regIdClasses) {
		if (regIdClasses == null) {
			return false;
		}
		return true;
	}

	@Override
	public List<Course> convertJsonToCourses(JSONArray jsonArray) {
//		JSONObject json = new JSONObject(regIdClasses); 		
		List<Course> courses = new ArrayList<Course>();
//		 JSONArray jsonArray = json.getJSONArray("regIdClasses");
		for (int i = 0; i < jsonArray.length(); i++) {
			Course course = courseService.findCourseById(jsonArray.getInt(i));
			courses.add(course);
		}
		return courses;
	}

	@Override
	public List<Integer> convertJsonArrayCoursesToRegIds(JSONArray jSONArray) {
		List<Integer> reg_ids = new ArrayList<Integer>();
		for (int i = 0; i < jSONArray.length(); i++) {
			reg_ids.add(jSONArray.getInt(i));
		}
		return reg_ids;
	}

	@Override
	public List<CourseDto> coursesToCourseDtos(List<Course> courses) {
		User user = userService.getCurrentLoggedUser();
		List<CourseDto> courseDtos = new ArrayList<CourseDto>();
		courses.forEach(e -> {
			String from = e.getCalendars().get(0).getStartTime().toString().substring(0, 5);
			String to = e.getCalendars().get(0).getEndTime().toString().substring(0, 5);
			String prerequisite;
			StudentCourse studentCourse = null;
			String registerStatus = null;
			Integer registerRank = null;
			try {
				prerequisite = e.getSubject().getPrerequisite().getSubjectCode().toString();
			} catch (Exception ex) {
				prerequisite = "None";
			}
			StudentCourseId studentCourseId = new StudentCourseId(user.getId(), e.getRegId());
			studentCourse = userCourseService.findById(studentCourseId);
			System.out.println(user.getId());
			if (studentCourse != null) {
				registerStatus = studentCourse.getUserCourseStatus().toString();
				registerRank = studentCourse.getWaitlistedRank();
			}
			String term_year = e.getStartDay().toString().substring(0, 4);
			CourseDto courseDto = new CourseDto(e.getRegId(), e.getSection(), e.getAvailable(), e.getCapacity(),
					e.getStartDay(), e.getEndDay(), e.getWaitlist(), e.getSubject().getSubjectCode().toString(),
					e.getRoom().getRoomName(), e.getTeacher().getName(), from, to, prerequisite,
					e.getTerm().getSemester().toString(), e.getSubject().getDescription(), term_year, registerStatus, registerRank);
			courseDtos.add(courseDto);
			
		});
		return courseDtos;

	}


	@Override
	public List<Course> findAll() {
		List<Course> courses = courseRepository.findAll();
		return courses;
	}

	@Override
	public List<CourseStudentDto> courseToCourseStudentDtos(Integer regId) {
		Course course = courseService.findCourseById(regId);
		List<StudentCourse> studentCourseList = course.getUsers();
		List<CourseStudentDto> courseStudentDtos = new ArrayList<CourseStudentDto>();
		studentCourseList.forEach(e -> {
			CourseStudentDto courseStudentDto = new CourseStudentDto(e.getUser().getUsername(), e.getUser().getEmail(), e.getUserCourseStatus().toString(), e.getCourse().getSubject().getSubjectCode().toString(), e.getCourse().getSubject().getDescription(), e.getWaitlistedRank(), e.getCourse().getCapacity(), e.getCourse().getAvailable());
			courseStudentDtos.add(courseStudentDto);
		});
		return courseStudentDtos;
	}

}