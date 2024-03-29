package com.bezkoder.springjwt.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.bezkoder.springjwt.models.FileDB;
import com.bezkoder.springjwt.payload.response.AssignmentResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.ResponseFile;
import com.bezkoder.springjwt.security.service.AssignmentService;
import com.bezkoder.springjwt.security.service.FileStorageService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

	@Autowired
	private FileStorageService storageService;

	@Autowired
	private AssignmentService assignmentService;

	// allow teacher to upload syllabus to a particular course
	@PostMapping("/upload")
	public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("regId") Integer regId) {
		String message = "";
		try {
			storageService.store(file, regId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}

	@PostMapping("/uploadAssignment")
	public ResponseEntity<MessageResponse> uploadAssignment(@RequestParam("file") MultipartFile file,
			@RequestParam("regId") Integer regId, @RequestParam("description") String description,
			@RequestParam("points") Integer points, @RequestParam("selectedDate") String selectedDate)
			throws ParseException {
		Date dueDate = new SimpleDateFormat("MM/dd/yyyy").parse(selectedDate);
		String message = "";
		try {
			assignmentService.storeAssignment(file, description, points, dueDate, regId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}

	}

	// get syllabus of a course
	@GetMapping(value = "/getFiles/{regId}")
	public ResponseEntity<List<ResponseFile>> getListFiles(@PathVariable("regId") Integer regId) {
		List<ResponseFile> files = storageService.getAllFilesById(regId).map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/files/")
					.path(dbFile.getId()).toUriString();

			return new ResponseFile(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping(value = "/getAssignments/{regId}")
	public ResponseEntity<List<AssignmentResponse>> getAllAssignmentsByCourse(@PathVariable("regId") Integer regId) {
		List<AssignmentResponse> files = assignmentService.getAllFilesById(regId).map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/assignments/")
					.path(dbFile.getId()).toUriString();

			return new AssignmentResponse(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length,
					dbFile.getDescription(), dbFile.getPoints(), dbFile.getDate());
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/assignments/{id}")
	public ResponseEntity<byte[]> getAssignment(@PathVariable String id) {
		FileDB fileDB = assignmentService.getAssignment(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		FileDB fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
				.body(fileDB.getData());
	}
}