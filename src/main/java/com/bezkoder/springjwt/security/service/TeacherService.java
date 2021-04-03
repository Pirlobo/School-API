package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;

@Service
public class TeacherService  implements ITeacherService{
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserCourseService userCourseService;
	
	@Autowired
	private StudentCourseRepository studentCourseRepository;

	@Override
	public List<StudentCourse> dropClasses(List<User> users, Integer regId) {
		List<StudentCourseId> userCourseIds = new ArrayList<StudentCourseId>();
		List<StudentCourse> userCourses = new ArrayList<StudentCourse>();
		Course course = courseService.findCourseById(regId);
		users.forEach(user -> {
			StudentCourseId userCourseId = new StudentCourseId(user.getId(), regId);
			userCourseIds.add(userCourseId);   
		});

		userCourseIds.forEach(e -> {
			StudentCourse userCourse = userCourseService.findById(e);
			userCourses.add(userCourse);
		});
		
		userCourses.forEach(e -> {
			if (e.getCourse().getWaitlist() <= 0) {
				List<StudentCourse> afterRankList = studentCourseRepository.findAfterRank(e.getWaitlistedRank());
				afterRankList.forEach(studentCourse -> {
					studentCourse.setWaitlistedRank(studentCourse.getWaitlistedRank() - 1);
				});
				studentCourseRepository.saveAll(afterRankList);
				course.setAvailable(e.getCourse().getAvailable() + 1);
				courseService.save(course);
				userCourseService.delete(e);
			} else {
				List<StudentCourse> afterRankList = studentCourseRepository.findAfterRank(e.getWaitlistedRank());
				afterRankList.forEach(studentCourse -> {
					studentCourse.setWaitlistedRank(studentCourse.getWaitlistedRank() - 1);
					if (studentCourse.getWaitlistedRank() <= e.getCourse().getCapacity()) {
						studentCourse.setUserCourseStatus(StudentCourseStatus.Successfull);
					}
				});
				studentCourseRepository.saveAll(afterRankList);
				course.setWaitlist(e.getCourse().getWaitlist() - 1);
				courseService.save(course);
				userCourseService.delete(e);
				
			}

		});
		return userCourses;

	}

}
