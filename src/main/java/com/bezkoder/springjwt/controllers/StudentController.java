package com.bezkoder.springjwt.controllers;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.payload.response.MessageResponse;

@RestController
public class StudentController {

	@GetMapping("/getAuthenthication")
	public ResponseEntity<?> subsequentInvokedUrl(){
		Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username =  ((UserDetails) principalObject).getUsername();
		if (username.equals("Anh")) {
			System.out.println("true");
			return ResponseEntity.ok(new MessageResponse("User is authenticated"));
		} else {
			System.out.println("false");
			return ResponseEntity.ok(new MessageResponse("User is not authenticated"));
		}
	}
}
