package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Orders;
import com.bezkoder.springjwt.book.PO;
import com.bezkoder.springjwt.book.RO;
import com.bezkoder.springjwt.dto.BookItemDto;
import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.dto.TranscriptDto;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.StudentCourseStatus;
import com.bezkoder.springjwt.models.Transcript;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.VerificationToken;
import com.bezkoder.springjwt.payload.request.EditProfileRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.persistence.PasswordResetTokenRepository;
import com.bezkoder.springjwt.persistence.StudentCourseRepository;
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

	@Autowired
	private StudentCourseRepository studentCourseRepository;

	@Value("${spring.mail.username}")
	private String email;

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		user.setVerificationToken(null);
		final VerificationToken myToken = new VerificationToken(token, user);
		verificationTokenRepository.save(myToken);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token, String code) {
		user.setPasswordResetToken(null);
		userService.save(user);
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		myToken.setCode(code);
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
		return String.format("%06d", number);
	}

	@Override
	public void registerForClassess(User user, List<Course> courses) {
		for (int i = 0; i < courses.size(); i++) {
			if (courseService.isAlaivable(courses.get(i))) {
				StudentCourse userCourse = new StudentCourse(user, courses.get(i));
				userCourse.setIsPassed(IsPassed.IP);
				userCourse.setWaitlistedRank(courses.get(i).getCapacity() - courses.get(i).getAvailable() + 1);
				userCourse.setUserCourseStatus(StudentCourseStatus.Successfull);
				courseService.setAvailable(courses.get(i).getAvailable() - 1, courses.get(i).getRegId());
				userCourseRepository.save(userCourse);

			} else {
				StudentCourse userCourse = new StudentCourse(user, courses.get(i));
				userCourse.setIsPassed(IsPassed.IP);
				userCourse.setUserCourseStatus(StudentCourseStatus.Wailisted);
				userCourse.setWaitlistedRank(courses.get(i).getCapacity() + courses.get(i).getWaitlist() + 1);
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
			userName = ((UserDetails) principal).getUsername();
			User user = userRepository.findByUsername(userName).orElse(null);
			return user;
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
				yourClasses.add(e.getCourse());
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
			if (userCourse.getIsPassed() == IsPassed.IP) {
				userCourses.add(userCourse);
			} else {
				throw new ResourceNotFoundException("Could not find");
			}
		});

		userCourses.forEach(e -> {
			if (e.getCourse().getWaitlist() <= 0) {
				List<StudentCourse> afterRankList = studentCourseRepository.findAfterRankOfClass(e.getWaitlistedRank(), e.getCourse().getRegId());
				afterRankList.forEach(studentCourse -> {
					studentCourse.setWaitlistedRank(studentCourse.getWaitlistedRank() - 1);
				});
				studentCourseRepository.saveAll(afterRankList);

				courseService.setAvailable(e.getCourse().getAvailable() + 1, e.getCourse().getRegId());
				courseService.save(e.getCourse());
				userCourseService.delete(e);
			} else {
				List<StudentCourse> afterRankList = studentCourseRepository.findAfterRankOfClass(e.getWaitlistedRank(),e.getCourse().getRegId());
				System.out.println(e.getWaitlistedRank() + "wailist nè");
				System.out.println(afterRankList.size() + "size nè");
				afterRankList.forEach(studentCourse -> {
					studentCourse.setWaitlistedRank(studentCourse.getWaitlistedRank() - 1);
					// 2 > 1
					if (studentCourse.getWaitlistedRank() <= e.getCourse().getCapacity()) {
						studentCourse.setUserCourseStatus(StudentCourseStatus.Successfull);
					}
					
				});
				studentCourseRepository.saveAll(afterRankList);
				courseService.setWailist(e.getCourse().getWaitlist() - 1, e.getCourse().getRegId());
				courseService.save(e.getCourse());
				userCourseService.delete(e);

			}

		});
		List<CourseDto> resultSet = courseService.coursesToCourseDtos(droppedClasses);
		return resultSet;

	}

	@Override
	public Orders placeOrder(List<BookItemDto> bookItemDto) {
		List<BookItems> setBook1 = new ArrayList<BookItems>();
		List<BookItems> setBook2 = new ArrayList<BookItems>();

		BookItems rentedBookItem = null;
		BookItems soldBookItem = null;

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
				total += soldBookItem.getRentalPrice() * (bookItemDto.get(i).getSellQuantity());

			}

		}
		order.setTotalPrice(total);

		orderService.save(order);
		bookItemService.saveAll(setBook1);
		bookItemService.saveAll(setBook2);
		return order;
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email).orElse(null);
		return user;
	}

	@Override
	public boolean existsByEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean existsByUsername(String userName) {
		if (userRepository.existsByUsername(userName)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public User findByUsername(String userName) {
		User user = userRepository.findByUsername(userName).orElse(null);
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		List<User> users = userRepository.findAll();
		User teacher = userRepository.findByEmail(this.email).orElse(null);
		users.remove(teacher);
		return users;
	}

	@Override
	public ResponseEntity<?> editProfile(EditProfileRequest editProfileRequest) {
		User user = userService.getCurrentLoggedUser();
		JSONObject json = new JSONObject(editProfileRequest);
		String email = (String) json.get("email");
		User user2 = userService.findByEmail(email);
		if (user2 != null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		} else {
			user.setEmail(email);
			userService.save(user);
			return ResponseEntity.ok(new MessageResponse("Saved"));
		}

	}

	@Override
	public List<TranscriptDto> getCourseGrades() {
		User user = getCurrentLoggedUser();
		List<TranscriptDto> transcriptDtos = new ArrayList<TranscriptDto>();
		List<StudentCourse> studentCourses = user.getCourses().stream()
				.filter(e -> e.getUserCourseStatus() == StudentCourseStatus.Successfull).collect(Collectors.toList());

		studentCourses.forEach(student -> {
			Character finalGrade;
			try {
				finalGrade = student.getFinalGrade().toString().charAt(0);
			} catch (Exception e) {
				finalGrade = null;
			}
			TranscriptDto transcriptDto;
			try {
				transcriptDto = new TranscriptDto(student.getCourse().getSubject().getSubjectCode().toString(),
						student.getGrade().toString().charAt(0), student.getPercentage(), finalGrade,
						student.getCourse().getTerm().getSemester().toString() + " "
								+ String.valueOf(student.getCourse().getTerm().getYear()));
				transcriptDtos.add(transcriptDto);
			} catch (Exception e) {
				transcriptDto = new TranscriptDto(student.getCourse().getSubject().getSubjectCode().toString(), null,
						null, finalGrade, student.getCourse().getTerm().getSemester().toString() + " "
								+ String.valueOf(student.getCourse().getTerm().getYear()));
				transcriptDtos.add(transcriptDto);
			}
		});
		
		return transcriptDtos;
	}
	
	@Override
	public Transcript getTranscript() {
		User user = getCurrentLoggedUser();
		Transcript transcript = user.getTranscript();
		return transcript;
	}

	@Override
	public List<StudentCourse> getCompletedCourses(String userName) {
		User user = userService.findByUsername(userName);
		List<StudentCourse> courses = user.getCourses();
		List<StudentCourse> finishedCourses = courses.stream().filter(e -> e.getIsPassed() != IsPassed.IP)
				.collect(Collectors.toList());

		return finishedCourses;
	}

	@Override
	public Integer getTotalEarnedCredits(String userName) {
		int totalEarnedCredits = 0;
		List<StudentCourse> finishedCourses = getCompletedCourses(userName);
		List<Course> completedClasses = new ArrayList<Course>();
		finishedCourses.forEach(e -> {
			completedClasses.add(e.getCourse());
		});
		for (int i = 0; i < completedClasses.size(); i++) {
			totalEarnedCredits += completedClasses.get(i).getUnits();
		}
		return totalEarnedCredits;
	}

	@Override
	public Integer getCorrespondingTotalGradePoints(String userName) {
		int totalTotalGradePoints = 0;
		List<StudentCourse> completedClasses = getCompletedCourses(userName);
		for (int i = 0; i < completedClasses.size(); i++) {
			switch (completedClasses.get(i).getFinalGrade()) {
			case A:
				totalTotalGradePoints += completedClasses.get(i).getCourse().getUnits() * 4;
				break;
			case B:
				totalTotalGradePoints += completedClasses.get(i).getCourse().getUnits() * 3;
				break;
			case C:
				totalTotalGradePoints += completedClasses.get(i).getCourse().getUnits() * 2;
				break;
			case D:
				totalTotalGradePoints += completedClasses.get(i).getCourse().getUnits() * 1;
				break;
			case F:
				totalTotalGradePoints += completedClasses.get(i).getCourse().getUnits() * 0;
				break;
			default:
				break;
			}
		}
		return totalTotalGradePoints;
	}
}