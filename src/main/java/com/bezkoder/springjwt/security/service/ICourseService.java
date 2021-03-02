package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.RegisterRequest;



@Service
public interface ICourseService {
	
	List<CourseDto> coursesToCourseDtos(List<Course> courses);
	
	List<Integer> convertJsonArrayCoursesToRegIds(JSONArray jSONArray);
	
	List<Course> convertJsonToCourses(JSONArray jSONArray);

	Course findCourseById(Integer id);
	
	// search classes by subject 
	List<CourseDto> searchCourses(String subject);

	// get all required books of a student
	List<Books> getAllRequiredBooks();

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

	// get all selected course can be registered
	List<Course> getSuccessfullRegisteredClasses(User user, List<Course> courses, List<Integer> regIdClasses);

	// is course repeated
	boolean isRepeated(User user, Course course);

	// check whether selected classes is/are duplicated
	boolean isDuplicated(List<Integer> regIdClasses);

	List<Course> getFilterdUnDuplicatedCourses(User user, List<Integer> regIdClasses);

	// check whether  the taken course is passed
	boolean isPassed(User user, Course course);

	
	boolean isNotTimeConflicted(User user, Course course, List<Integer> regIdClasses);

	// check whether the selected course is preregquisited
	boolean isPreregquisited(User user, Course course);

	// search classes by subject
	List<Course> findBySubject(String subjectName);

	// check is any selected course conflicted 
	public boolean isCourseConflicted(User user, List<Integer> regIdClasses);

	// return a list of courses which are not conflicted to be able to register
	public List<Course> getUnconflictedCourse(User user, List<Integer> regIdClasses);

	
	// check whether selected courses is/are already registered ? 
	public Integer checkRegisteredCourses(User user, List<Integer> regIdClasses);

	// check at least one class is selected in order to register
	public boolean isAnyCourseSelected(List<Integer> regIdClasses);


}

//1 -> getUnconflictedCourses are all registered
	// 2 -> getUnconflictedCourses require pre course
	// 3-> One or more of the selected courses is already registered
	// 4 -> getUnconflictedCourses.size() = 0

