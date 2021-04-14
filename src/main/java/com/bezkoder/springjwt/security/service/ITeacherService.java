package com.bezkoder.springjwt.security.service;
import java.util.List;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.User;

public interface ITeacherService {
	
	public List<StudentCourse> dropClasses(List<User> users, Integer regId) ;

}
