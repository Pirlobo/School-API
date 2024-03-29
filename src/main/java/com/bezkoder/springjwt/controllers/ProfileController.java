package com.bezkoder.springjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bezkoder.springjwt.payload.request.EditProfileRequest;
import com.bezkoder.springjwt.security.service.UserService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

	@Autowired
	private UserService userService;

	@PostMapping("/editProfile")
	public ResponseEntity<?> editProfile(@RequestBody EditProfileRequest editProfileRequest) {
		ResponseEntity<?> responseEntity = userService.editProfile(editProfileRequest);
		return responseEntity;
	}
}
