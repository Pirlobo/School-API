package com.bezkoder.springjwt.security.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.book.BookItemDto;
import com.bezkoder.springjwt.book.Orders;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.VerificationToken;

@Service
public interface IUserService {

	// create password reset token for a student
		void createPasswordResetTokenForUser(User user, String token, String code);


	// create verification token for a student
	void createVerificationTokenForUser(User user, String token);


	VerificationToken getVerificationToken(String token);


	PasswordResetToken getResetPasswordResetToken(String token);
	
	
		// register for classes
		void registerForClassess(User user, List<Course> courses);

		// get current logged student
		User getCurrentLoggedUser();
		
		// get all taken or registered classes
		List<Course> getYourClasses(User user);
		
		// drop classes
		public List<CourseDto> dropClasses(User user, List<Integer> regIdClasses);
		
		
		// place order from buying or renting Book Items
		Orders placeOrder(List<BookItemDto> bookItemDto);


}
