package com.bezkoder.springjwt.security.service;
import java.util.List;

import com.bezkoder.springjwt.dto.GradeDto;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.Teacher;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.AnnouncementRequest;
import com.bezkoder.springjwt.payload.request.GradeRequest;

public interface ITeacherService {
	
	public List<StudentCourse> dropClasses(List<User> users, Integer regId) ; 

	public Teacher findTeacherByUser (User user);
	
	public void grade(GradeRequest gradeRequest);
	
	public List<GradeDto> getAllStudentsByCourseForGrading(Integer regId);
	
	public GradeDto findGradedStudentByName(Integer regId, String studentName);

	public void sendAnnouncementToAllStudents(AnnouncementRequest announcementRequest);
	
}
