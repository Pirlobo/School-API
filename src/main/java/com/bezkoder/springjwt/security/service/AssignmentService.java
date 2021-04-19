package com.bezkoder.springjwt.security.service;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.bezkoder.springjwt.models.Assignment;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.FileDB;
import com.bezkoder.springjwt.persistence.AssignmentRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;

@Service
public class AssignmentService implements IAssignmentService {
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	public void storeAssignment(MultipartFile file, String description, Integer points, Date dueDate, Integer regId)
			throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Course course = courseRepository.findById(regId).orElse(null);
		dueDate.setDate(dueDate.getDate() + 1);
		Assignment assignment = new Assignment(description, points, dueDate, fileName, file.getContentType(),
				file.getBytes(), course);
		course.addAssignment(assignment);
		assignmentRepository.save(assignment);

	}
	public Stream<Assignment> getAllFilesById(Integer regId) {
	    return assignmentRepository.getAllFilesByCourseId(regId).stream();
	  }
}
