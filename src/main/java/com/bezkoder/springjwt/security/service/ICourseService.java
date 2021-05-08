package com.bezkoder.springjwt.security.service;

import java.util.List;

import java.util.Set;
import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.dto.BookDto;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.CourseStudentDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.User;

@Service
public interface ICourseService {

	// get all courses which has IP status (In Progress)
	List<Course> getIPCourses();

	// convert to dto object
	List<CourseDto> coursesToCourseDtos(List<Course> courses);

	// convert to dto object
	List<Integer> convertJsonArrayCoursesToRegIds(JSONArray jSONArray);

	// convert to dto object
	List<Course> convertJsonToCourses(JSONArray jSONArray);

	// find Course by Id
	Course findCourseById(Integer id);

	// search classes by subject
	List<CourseDto> searchCourses(String subject);

	// get all required books of a student
	Set<BookDto> getAllRequiredBooks();

	// get all registered classes of a student
	public List<Course> findRegisteredClasses(List<Integer> regIdClasses);

	// is selected classe available to register
	boolean isAlaivable(Course course);

	// save course
	void save(Course course);

	// set available
	void setAvailable(Integer available, Integer id);

	// set waitist
	void setWailist(Integer wailist, Integer id);

	// get all selected courses can not be registered
	Set<Course> getFailedRegisteredClasses(User user, List<Course> courses, List<Integer> regIdClasses);

	// is course repeated
	boolean isRepeated(User user, Course course);

	// check whether selected classes is/are duplicated
	boolean isDuplicated(List<Integer> regIdClasses);

	// check whether the taken course is passed
	boolean isPassed(User user, Course course);

	boolean isNotTimeConflicted(User user, Course course, List<Integer> regIdClasses);

	// check whether the selected course is preregquisited
	boolean isPreregquisited(User user, Course course);

	// search classes by subject
	List<Course> findBySubject(String subjectName);

	// check at least one class is selected in order to register
	public boolean isAnyCourseSelected(List<Integer> regIdClasses);

	// find all courses
	List<Course> findAll();

	// convert to dto object
	List<CourseStudentDto> courseToCourseStudentDtos(Integer regId);

	// get the first ? pageSize courses for every page and sorted by Id
	Page<Course> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	// get the first ? pageSize courses for every page and sorted by Id with writing query
	public Page<Course> findPaginatedByTitle(Integer year, String title, int pageNo, int pageSize, String sortField,
			String sortDirection);

	List<Course> getIntendedDroppdeCourses();

}
