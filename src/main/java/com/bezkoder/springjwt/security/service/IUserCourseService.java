package com.bezkoder.springjwt.security.service;

import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;

@Service
public interface IUserCourseService {

	// find student_course object by id
	StudentCourse findById(StudentCourseId userCourseId);

	// persist student_course
	void save(StudentCourse userCourse);

	// delete student_course
	void delete(StudentCourse userCourse);
}
