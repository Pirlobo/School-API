package com.bezkoder.springjwt.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.StudentDto;
import com.bezkoder.springjwt.models.VerificationToken;
import com.bezkoder.springjwt.payload.request.AnnouncementRequest;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.ResetPasswordRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.ResetPasswordResponse;
import com.bezkoder.springjwt.security.event.OnAnnouncementEvent;
import com.bezkoder.springjwt.security.event.OnRegistrationEvent;
import com.bezkoder.springjwt.security.event.OnResetPasswordEvent;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.PasswordResetTokenService;
import com.bezkoder.springjwt.security.service.UserService;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	@Autowired
	private CourseService courseService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest req) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		char c = 'a';
		System.out.println(++c);
//		for (c = 'a'; c <= 'z'; ++c) {
//			System.out.println(c + " ");
//		}

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId().intValue(), userDetails.getUsername(),
				userDetails.getEmail(), roles, userDetails.isActive()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
			final HttpServletRequest request) {
		if (userService.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userService.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<Role> roles = new HashSet<>();
		Role role1 = new Role(ERole.ROLE_USER);
		roles.add(role1);
		user.getRoles().add(role1);
		userService.save(user);

		final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();

		eventPublisher.publishEvent(new OnRegistrationEvent(user, appUrl));

		return ResponseEntity.ok(new MessageResponse("Please, check your email to activate your account"));
	}

	@GetMapping(value = "/registrationConfirm")
	public void confirmRegistration(HttpServletResponse response, @RequestParam("token") final String token,
			final RedirectAttributes redirectAttributes) throws IOException {
		final VerificationToken verificationToken = userService.getVerificationToken(token);
		final User user = verificationToken.getUser();
		user.setActive(true);
		userService.save(user);
		response.sendRedirect("http://localhost:8081/login");
	}

	@GetMapping(value = "/resetPasswordConfirm")
	public void confirmResetPasswordResquest(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam("token") final String token, final RedirectAttributes redirectAttributes) throws IOException {
		try {
			PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
			if (passwordResetToken == null) {
				response.sendRedirect("http://localhost:8082/login");
			} else {
				response.sendRedirect("http://localhost:8082/resetPassword");
			}
		} catch (Exception e) {
			response.sendRedirect("http://localhost:8082/login");
		}

	}

	@PostMapping("/isUserActive")
	public ResponseEntity<?> isUserActive(@Valid @RequestBody String username) {
		try {
			JSONObject json = new JSONObject(username);
			String fetchedUserName = json.getString("username");
			User user = userService.findByUsername(fetchedUserName);
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
		User user = userService.findByEmail(fetchedEmail);
		System.out.println(user);
		if (user == null || !user.isActive()) {
			return ResponseEntity.ok(new MessageResponse("Can not find user with the given email"));
		}
		final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();

		eventPublisher.publishEvent(new OnResetPasswordEvent(user, appUrl));
		PasswordResetToken passwordResetToken = passwordResetTokenService.findByUser(user);

		String token = passwordResetToken.getToken();
		return ResponseEntity.ok(new ResetPasswordResponse(token));

	}

	@PostMapping("/findByCode")
	public ResponseEntity<?> findByToken(@RequestBody final String code) {
		try {
			JSONObject json = new JSONObject(code);
			String fetchedCode = json.getString("code");
			PasswordResetToken passwordResetToken = passwordResetTokenService.findByCode(fetchedCode);
			System.out.println(fetchedCode);
			User user = passwordResetToken.getUser();

			return ResponseEntity.ok(new StudentDto(fetchedCode, user.getEmail(), user.getUsername()));
		} catch (Exception e) {
			return new ResponseEntity(new MessageResponse("Not Found"), HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		try {
			PasswordResetToken passwordResetToken = passwordResetTokenService
					.findByCode(resetPasswordRequest.getCode());
			User user = passwordResetToken.getUser();
			if (user.getEmail().equals(resetPasswordRequest.getEmail())) {
				String newPassword = resetPasswordRequest.getPassword();
				user.setPassword(encoder.encode(newPassword));
				userService.save(user);
				return ResponseEntity.ok(new MessageResponse("Saved"));
			} else {
				return ResponseEntity.ok(new MessageResponse("Not Found"));
			}
		} catch (Exception e) {
			return ResponseEntity.ok(new MessageResponse("Not Found"));
		}
	}


}