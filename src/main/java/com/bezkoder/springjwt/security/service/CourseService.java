package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Flags.Flag;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.CourseStudentDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.CoursePaginationRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;

@Service
public class CourseService implements ICourseService {
	@Autowired
	private CoursePaginationRepository coursePaginationRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserService userService;

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
		List<Course> courseList = courseRepository.findByTitle(year + 1, title);
		List<CourseDto> resultSet = convert(courseList);
		return resultSet;
	}

	public List<CourseDto> convert(List<Course> courseList) {
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
	public Set<Books> getAllRequiredBooks() {
		Set<Books> bookList = new HashSet<Books>();
		List<Course> courses = courseService.getIPCourses();
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
			boolean isPreregquisited = (isPreregquisited(user, e));
			if (isPreregquisited == false) {
				System.out.println("a");
				failedRegisteredClasses.add(e);
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
			if (registeredCourses.size() == 0) {
				return false;
			}
			for (int i = 0; i < registeredCourses.size(); i++) {
				StudentCourse userCourse = studentCourseRepository.findStudentCourseById(user.getId(),
						registeredCourses.get(i).getRegId());
				if (registeredCourses.get(i).getSubject().getSubjectCode() == course.getSubject().getPrerequisite()
						.getSubjectCode()) {
					if (userCourse.getIsPassed() == IsPassed.TRUE) {
						continue;
					} else {
						return false;
					}
				}
			}
			return true;
		} else {
			return true;
		}
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
			if (currentCourses.get(i).getSubject().getSubjectCode().toString()
					.equals(course.getSubject().getSubjectCode().toString())) {

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
			return true;
		}
	}

	public List<Course> getUnconflictedCourse(User user, List<Integer> regIdClasses) {
		List<Course> unConflictedCourses = new ArrayList<Course>();
		List<Course> registeredClasses = courseService.findRegisteredClasses(regIdClasses);
		for (int i = 0; i < registeredClasses.size(); i++) {
			if (courseService.isNotTimeConflicted(user, registeredClasses.get(i), regIdClasses)) {
				unConflictedCourses.add(registeredClasses.get(i));

			}
		}
		return unConflictedCourses;
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
			if (studentCourse != null) {
				registerStatus = studentCourse.getUserCourseStatus().toString();
				registerRank = studentCourse.getWaitlistedRank();
			}
			String term_year = e.getStartDay().toString().substring(0, 4);
			CourseDto courseDto = new CourseDto(e.getRegId(), e.getSection(), e.getAvailable(), e.getCapacity(),
					e.getStartDay(), e.getEndDay(), e.getWaitlist(), e.getSubject().getSubjectCode().toString(),
					e.getRoom().getRoomName(), e.getTeacher().getName(), from, to, prerequisite,
					e.getTerm().getSemester().toString(), e.getSubject().getDescription(), term_year, registerStatus,
					registerRank);
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
			CourseStudentDto courseStudentDto = new CourseStudentDto(e.getUser().getUsername(), e.getUser().getEmail(),
					e.getUserCourseStatus().toString(), e.getCourse().getSubject().getSubjectCode().toString(),
					e.getCourse().getSubject().getDescription(), e.getWaitlistedRank(), e.getCourse().getCapacity(),
					e.getCourse().getAvailable());
			courseStudentDtos.add(courseStudentDto);
		});
		return courseStudentDtos;
	}

	@Override
	public Page<Course> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return courseRepository.findAll(pageable);
	}

	@Override
	public Page<Course> findPaginatedByTitle(Integer year, String search, int pageNo, int pageSize, String sortField,
			String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Page<Course> coursePage = coursePaginationRepository.findAllByTitle(year, search, pageable);
		return coursePage;
	}

	@Override
	public List<Course> getIPCourses() {
		User user = userService.getCurrentLoggedUser();
		List<StudentCourse> unFilteredList = user.getCourses();

		List<StudentCourse> filteredList = unFilteredList.stream().filter(c -> c.getIsPassed() == IsPassed.IP)
				.collect(Collectors.toList());

		List<Course> IPCourses = new ArrayList<Course>();
		for (StudentCourse studentCourse : filteredList) {
			IPCourses.add(studentCourse.getCourse());
		}
		return IPCourses;
	}

}