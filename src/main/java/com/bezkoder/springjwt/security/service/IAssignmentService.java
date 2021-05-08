package com.bezkoder.springjwt.security.service;

import java.io.IOException;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IAssignmentService {
	public void storeAssignment(MultipartFile file, String description, Integer points, Date dueDate, Integer regId) throws IOException; 
}
