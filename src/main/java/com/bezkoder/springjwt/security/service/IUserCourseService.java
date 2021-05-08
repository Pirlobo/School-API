package com.bezkoder.springjwt.security.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.Grade;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.User;

@Service
public interface IUserCourseService {
	
	// find student_course object by id
	List<StudentCourse> findAllStudentsByCourse(Integer regId);

	// find student_course object by id
	StudentCourse findById(StudentCourseId userCourseId);

	// persist student_course
	void save(StudentCourse userCourse);

	// delete student_course
	void delete(StudentCourse userCourse);
	
	// teacher grades a student
	void update(Grade letter, Double percentage, User userId, Course coureRegId);
	
	// teacher gives student a final grade
	void lastUpdate(Grade letter, Double percentage, Grade finalGrade, User userId, Course coureRegId);
	
	// set IsPassed Status (student whether passed or failed the class)
	void setIsPassedStatus(IsPassed isPassed, User userId, Course coureRegId);
}
