package com.bezkoder.springjwt.security.service;

import java.sql.Date;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.bezkoder.springjwt.book.BookItemDto;
import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Orders;
import com.bezkoder.springjwt.book.PO;
import com.bezkoder.springjwt.book.RO;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.VerificationToken;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.PasswordResetTokenRepository;
import com.bezkoder.springjwt.persistence.UserCourseRepository;
import com.bezkoder.springjwt.persistence.UserRepository;
import com.bezkoder.springjwt.persistence.VerificationTokenRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;


@Service
@Transactional
public class UserService implements IUserService {

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserCourseRepository userCourseRepository;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserCourseService userCourseService;
	
	@Autowired
	private CalendarService calendarService;
	
	@Autowired
	private BookItemService bookItemService;
	
	@Autowired
	private OrderService orderService;
	
	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		user.setVerificationToken(null);
		final VerificationToken myToken = new VerificationToken(token, user);
		verificationTokenRepository.save(myToken);
		
	}
	
	@Override
	public void createPasswordResetTokenForUser(User user, String token, String code) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		myToken.setCode(code);
		user.setPasswordResetToken(null);
		passwordResetTokenRepository.save(myToken);
	}
	@Override
	public VerificationToken getVerificationToken(final String token) {
		return verificationTokenRepository.findByAccessToken(token);
	}
	
	@Override
	public PasswordResetToken getResetPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	
	public String generateCode() {
		 // Randomly generate 6 digits as a code
	    // from 0 to 999999
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);

	    // this will convert any number sequence into 6 character.
	    return String.format("%06d", number);
	}

	@Override
	public void registerForClassess(User user, List<Course> courses) {
		for (int i = 0; i < courses.size(); i++) {
			if (courseService.isAlaivable(courses.get(i))) {
				StudentCourse userCourse = new StudentCourse(user, courses.get(i));
				userCourse.setUserCourseStatus(StudentCourseStatus.Successfull);
				courseService.setAvailable(courses.get(i).getAvailable() - 1, courses.get(i).getRegId());
				userCourseRepository.save(userCourse);

			} else {
				StudentCourse userCourse = new StudentCourse(user, courses.get(i));
				userCourse.setUserCourseStatus(StudentCourseStatus.Wailisted);
				courseService.setWailist(courses.get(i).getWaitlist() + 1, courses.get(i).getRegId());
				userCourseRepository.save(userCourse);
				

			}
		}
	

	}

	@Override
	public User getCurrentLoggedUser() {
		String userName;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetailsImpl) {
//			userName = ((UserDetails) principal).getUsername();
//			User user = userRepository.findByEmail(userName).orElse(null);
//			return user;
			System.out.println(principal);
		}
		return null;
	}

	@Override
	// @Cacheable(value = "userCache")
	public List<Course> getYourClasses(User user) {
		List<Course> yourClasses = new ArrayList<Course>();
		try {
			List<StudentCourse> userCourses = user.getCourses();
			userCourses.forEach(e -> {
				if (e.getUserCourseStatus().equals(StudentCourseStatus.Successfull)) {
					yourClasses.add(e.getCourse());
				}

			});
		} catch (Exception e) {
			return yourClasses;
		}
		return yourClasses;

	}

	@Override
	public List<CourseDto> dropClasses(User user, List<Integer> regIdClasses) {
		List<Course> droppedClasses = courseService.findRegisteredClasses(regIdClasses);
		List<StudentCourseId> userCourseIds = new ArrayList<StudentCourseId>(); 
		
		droppedClasses.forEach(e -> {
			StudentCourseId userCourseId = new StudentCourseId(user.getId(), e.getRegId());
			userCourseIds.add(userCourseId);
		});
		List<StudentCourse> userCourses = new ArrayList<StudentCourse>();

		userCourseIds.forEach(e -> {
			StudentCourse userCourse = userCourseService.findById(e);
			userCourses.add(userCourse);
		});
		userCourses.forEach(e -> {
			courseService.setAvailable(e.getCourse().getAvailable() + 1, e.getCourse().getRegId());
			userCourseService.delete(e);
			
		});
		List<CourseDto> resultSet = courseService.coursesToCourseDtos(droppedClasses);
		return resultSet;

	}

	@Override
	public Orders placeOrder(List<BookItemDto> bookItemDto) {
		List<BookItems> setBook1 = new ArrayList<BookItems>();
		List<BookItems> setBook2 = new ArrayList<BookItems>();
		
		BookItems rentedBookItem = null;
		BookItems soldBookItem = null ;
		
		double total = 0.0;
		
		Orders order = new Orders(userService.getCurrentLoggedUser(), total);
		for (int i = 0; i < bookItemDto.size(); i++) {
			if (bookItemDto.get(i).isRented() == true) {
				RO ro = new RO(calendarService.getDefaultDueDate(), calendarService.getCurrentTime(), null);
				order.addRO(ro);
				rentedBookItem = bookItemService.findById(bookItemDto.get(i).getBarcode()); 
				rentedBookItem.addRO(ro);
				setBook1.add(rentedBookItem);
				total += rentedBookItem.getRentalPrice();
			}
			if (bookItemDto.get(i).isSold() == true) {
				PO po = new PO(bookItemDto.get(i).getSellQuantity());
				order.addPO(po);
				soldBookItem = bookItemService.findById(bookItemDto.get(i).getBarcode());
				soldBookItem.addPO(po);
				setBook2.add(soldBookItem);
				total += soldBookItem.getRentalPrice()*(bookItemDto.get(i).getSellQuantity());
			
			}
			    
		}
		order.setTotalPrice(total);
		
		orderService.save(order);
		bookItemService.saveAll(setBook1);
		bookItemService.saveAll(setBook2);
		return order;
	}
	
}
