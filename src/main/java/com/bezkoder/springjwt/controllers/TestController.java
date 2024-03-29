//package com.bezkoder.springjwt.controllers;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
////@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("/api/test")
//public class TestController {
//	@GetMapping("/all")
//	public String allAccess() {
//		return "Public Content.";
//	}
//	@GetMapping("/demo")
//	public boolean demo() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication == null || AnonymousAuthenticationToken.class.
//			      isAssignableFrom(authentication.getClass())) {
//			        System.out.println("Null nè");;
//			    }
//		return authentication.isAuthenticated();
//	}
//	
//	
//	@GetMapping("/user")
//	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//	public String userAccess() {
//		return "User Content.";
//	}
//
//	@GetMapping("/mod")
//	@PreAuthorize("hasAuthority('MODERATOR')")
//	public String moderatorAccess() {
//		return "Moderator Board.";
//	}
//
//	@GetMapping("/admin")
//	@PreAuthorize("hasRole('ADMIN')")
//	public String adminAccess() {
//		return "Admin Board.";
//	}
//}
