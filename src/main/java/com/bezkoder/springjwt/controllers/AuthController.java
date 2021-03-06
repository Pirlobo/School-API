package com.bezkoder.springjwt.controllers;

import java.io.IOException;
import java.net.http.HttpRequest;

import javax.servlet.ServletContext;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.bouncycastle.asn1.ocsp.ResponderID;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.StudentDto;
import com.bezkoder.springjwt.models.VerificationToken;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.ResetPasswordRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.ResetPasswordResponse;
import com.bezkoder.springjwt.persistence.PasswordResetTokenRepository;
import com.bezkoder.springjwt.persistence.RoleRepository;
import com.bezkoder.springjwt.persistence.UserRepository;
import com.bezkoder.springjwt.persistence.VerificationTokenRepository;
import com.bezkoder.springjwt.security.event.OnRegistrationEvent;
import com.bezkoder.springjwt.security.event.OnResetPasswordEvent;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.UserService;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;

import groovy.util.ObjectGraphBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import javassist.expr.NewArray;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest req) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		System.out.println(authentication.getName());

		User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
	
		String jwt = jwtUtils.generateJwtToken(authentication);
		
//		VerificationToken verificationToken = new VerificationToken(jwt, user);
//		verificationTokenRepository.save(verificationToken);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//		org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		User user3 = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
		System.out.println(user3.isActive());

//		HttpSession session = req.getSession(true);
//	    session.setAttribute("SPRING_SECURITY_CONTEXT_KEY", SecurityContextHolder.getContext());
		
		return ResponseEntity.ok(new JwtResponse(jwt , userDetails.getId().intValue(), userDetails.getUsername(),
				userDetails.getEmail(), roles, user3.isActive()));
	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
			final HttpServletRequest request) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<Role> roles = new HashSet<>();
		Role role1 = new Role(ERole.ROLE_USER);
		roles.add(role1);
		user.getRoles().add(role1);
		userRepository.save(user);

		final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		eventPublisher.publishEvent(new OnRegistrationEvent(user, appUrl));

		return ResponseEntity.ok(new MessageResponse("Please, check your email to activate your account"));
	}

	@GetMapping(value = "/registrationConfirm")
	public void confirmRegistration(HttpServletResponse response, @RequestParam("token") final String token,
			final RedirectAttributes redirectAttributes) throws IOException {
		final VerificationToken verificationToken = userService.getVerificationToken(token);
		final User user = verificationToken.getUser();
		user.setActive(true);
		userRepository.save(user);
		response.sendRedirect("http://localhost:8081/login");
	}

	@GetMapping(value = "/resetPasswordConfirm")
	public void confirmResetPasswordResquest( final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("token") final String token, final RedirectAttributes redirectAttributes) throws IOException {
		try {
			PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
			if (passwordResetToken == null) {
				response.sendRedirect("http://localhost:8081/login");
			}
			else {
//				Cookie cookie = new Cookie("username", "Jovan");
//				cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
//				cookie.setSecure(true);
//				cookie.setHttpOnly(true);
//				cookie.setPath("http://localhost:8081/resetPassword"); // global cookie accessible every where
//				response.addCookie(cookie);
     		response.sendRedirect("http://localhost:8081/resetPassword");
			}
		} catch (Exception e) {
			response.sendRedirect("http://localhost:8081/login");
		}
	
	}

	@PostMapping("/isUserActive")
	public ResponseEntity<?> isUserActive(@Valid @RequestBody String username) {
		try {
			JSONObject json = new JSONObject(username);
			String fetchedUserName = json.getString("username");
			User user = userRepository.findByUsername(fetchedUserName).orElse(null);
			return ResponseEntity.ok(new User(user.getUsername(), user.getEmail(), user.getPassword()));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Can't find user with the given username"));
		}
	}

	@PostMapping("/sendEmail")
	public ResponseEntity<?> sendEmail(@Valid @RequestBody @Email String email, final HttpServletRequest request) {

		JSONObject json = new JSONObject(email);
		String fetchedEmail = json.getString("email");
		User user = userRepository.findByEmail(fetchedEmail).orElse(null);
		System.out.println(user);
		if (user == null || !user.isActive()) {
			return ResponseEntity.ok(new MessageResponse("Can not find user with the given email"));
		}
		final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();

		eventPublisher.publishEvent(new OnResetPasswordEvent(user, appUrl));
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user);
		
		String token = passwordResetToken.getToken();
		return ResponseEntity.ok(new ResetPasswordResponse(token));

	}
	
	@PostMapping("/findByCode")
	public ResponseEntity<?> findByToken(@RequestBody final String code) {
		try {
			JSONObject json = new JSONObject(code); 
			String fetchedCode = json.getString("code");
//			String filterFetchedTokenString = fetchedToken.substring(1, fetchedToken.length()-1);
			PasswordResetToken passwordResetToken  = passwordResetTokenRepository.findByCode(fetchedCode);
			System.out.println(fetchedCode);
			User user = passwordResetToken.getUser();
			
			return ResponseEntity.ok(new StudentDto(fetchedCode, user.getEmail(), user.getUsername()));
		} catch (Exception e) {
//		return ResponseEntity
//        .status(HttpStatus.NOT_FOUND)
//        .body("404");
			return new ResponseEntity(new MessageResponse("Not Found"), HttpStatus.NOT_FOUND);
		}
		
	}


	
	
	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
		try {
			System.out.println(resetPasswordRequest.getCode());
			PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByCode(resetPasswordRequest.getCode());
			
			User user = passwordResetToken.getUser();
			
			System.out.println(user.getPassword() + "aefafeaefa");
			
			if (user.getEmail().equals(resetPasswordRequest.getEmail())) {
				String newPassword  = resetPasswordRequest.getPassword();
				user.setPassword(encoder.encode(newPassword));
				userRepository.save(user);
				return ResponseEntity.ok(new MessageResponse("Saved"));
			}
			else {
				return ResponseEntity.ok(new MessageResponse("Not Found"));
			}
		} catch (Exception e) {
			return ResponseEntity.ok(new MessageResponse("Not Found"));
		}
	}

}