package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.List;

import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.User;

public interface ITeacherService {
	
	public List<StudentCourse> dropClasses(List<User> users, Integer regId) ;

}
