package com.bezkoder.springjwt.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.Grade;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.UserCourseRepository;

@Service
public class UserCourseService implements IUserCourseService {

	@Autowired
	private UserCourseRepository userCourseRepository;

	@Autowired
	private CourseService courseService;

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

	@Override
	public List<StudentCourse> findAllStudentsByCourse(Integer regId) {
		Course course = courseService.findCourseById(regId);
		List<StudentCourse> studentCourseList = course.getUsers();
		List<StudentCourse> gradedStudents = studentCourseList.stream()
				.filter(student -> student.getUserCourseStatus() == StudentCourseStatus.Successfull)
				.collect(Collectors.toList());
		return gradedStudents;
	}

	@Override
	public void update(Grade letter, Double percentage, User userId, Course coureRegId) {
		userCourseRepository.update(letter, percentage, userId, coureRegId);
	}

	@Override
	public void lastUpdate(Grade letter, Double percentage, Grade finalGrade, User userId, Course coureRegId) {
		userCourseRepository.lastUpdate(letter, percentage, finalGrade, userId, coureRegId);

	}

	@Override
	public void setIsPassedStatus(IsPassed isPassed, User userId, Course coureRegId) {
		userCourseRepository.setIsPassedStatus(isPassed, userId, coureRegId);

	}

}
