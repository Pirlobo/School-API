package com.bezkoder.springjwt.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.persistence.UserCourseRepository;

@Service
public class UserCourseService implements IUserCourseService {

	@Autowired
	private UserCourseRepository userCourseRepository;
	
	@Override
	public StudentCourse findById(StudentCourseId userCourseId) {
		StudentCourse userCourse = userCourseRepository.findById(userCourseId).orElse(null);
		return userCourse;
	}

	@Override
	public void save(StudentCourse userCourse) {
		userCourseRepository.save(userCourse);
		
	}

	@Override
	public void delete(StudentCourse userCourse) {
		userCourseRepository.delete(userCourse);
		
	}
	
}
