package com.bezkoder.springjwt;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bezkoder.springjwt.book.Authors;
import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.book.Format;
import com.bezkoder.springjwt.models.Building;
import com.bezkoder.springjwt.models.Classroom;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.Room;
import com.bezkoder.springjwt.models.Semester;
import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.SubjectCode;
import com.bezkoder.springjwt.models.SubjectName;
import com.bezkoder.springjwt.models.Teacher;
import com.bezkoder.springjwt.models.Term;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.BookItemsRepository;
import com.bezkoder.springjwt.persistence.BookRepository;
import com.bezkoder.springjwt.persistence.BuildingRepository;
import com.bezkoder.springjwt.persistence.ClassroomRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.RoleRepository;
import com.bezkoder.springjwt.persistence.RoomRepository;
import com.bezkoder.springjwt.persistence.SubjectRepository;
import com.bezkoder.springjwt.persistence.TeacherRepository;
import com.bezkoder.springjwt.persistence.TermRepository;
import com.bezkoder.springjwt.security.service.UserService;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class SpringBootSecurityJwtApplication {
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	public BookRepository bookRepository;

	@Autowired
	private BookItemsRepository bookItemsRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private ClassroomRepository classroomRepository;

	@Autowired
	private TermRepository termRepository;

	@Autowired
	private UserService userService;

	@Autowired
	PasswordEncoder encoder;

	private static final Logger logger = LoggerFactory.getLogger(SpringBootSecurityJwtApplication.class);

	@Value("${spring.mail.username}")
	private String email;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

	@PostConstruct
	public void init() {

		

		// new an object of Teacher

		Building building1 = new Building(1, "MS");
		List<Room> rooms = new ArrayList<Room>();
		Room room1 = new Room(1, "MS302", building1);
		Room room2 = new Room(2, "MS303", building1);

		rooms.add(room1);
		rooms.add(room2);

		buildingRepository.save(building1);
		roomRepository.saveAll(rooms);

		// create an instructor
		try {
			User user = new User("Anh", this.email, encoder.encode("teacher"));

			Set<Role> roles = new HashSet<>();
			Role role1 = new Role(ERole.ROLE_TEACHER);
			roles.add(role1);
			user.getRoles().add(role1);
			user.setActive(true);
			userService.save(user);
			
			Teacher teacher1 = new Teacher(1, user);
			teacherRepository.save(teacher1);

			Date startDay = new Date(120, 8, 15);
			Date endDay = new Date(120, 11, 20);

			Date startDay1 = new Date(119, 12, 25);
			Date endDay1 = new Date(120, 4, 27);

			// new Subject

			String description_comsc_72 = "Discrete Structures";
			String description_comsc_79 = "Programming In C";
			String description_comsc_75 = "Introduction To Programming (Java)";
			String description_comsc_76 = "Introduction To Data Structure (Java)";
			String description_comsc_77 = "Introduction to Computer Systems";
			Subject subject2 = new Subject(2, SubjectName.Computer_Science, SubjectCode.COMSC_75, null,
					description_comsc_75);
			subjectRepository.save(subject2);
			
			Subject subject = new Subject(1, SubjectName.Computer_Science, SubjectCode.COMSC_76, subject2,
					description_comsc_76);
			subjectRepository.save(subject);
			
			Subject subject3 = new Subject(3, SubjectName.Computer_Science, SubjectCode.COMSC_77, subject2,
					description_comsc_77);
			subjectRepository.save(subject3);
			
			Subject comsc72 = new Subject(4, SubjectName.Computer_Science, SubjectCode.COMSC_72, null,
					description_comsc_72);
			subjectRepository.save(comsc72);
			
			Subject comsc79 = new Subject(5, SubjectName.Computer_Science, SubjectCode.CIS_54, null,
					description_comsc_79);
			subjectRepository.save(comsc79);

			Term term = new Term(1, Semester.Fall, 2022);
			Term term2 = new Term(2, Semester.Spring, 2022);
			termRepository.save(term);
			termRepository.save(term2);

			// new courses with fk_teacher
			Course cs79 = new Course(10101, 101, 35, new Date(term2.getYear() - 1900 - 1, 12, 15), new Date(term2.getYear() - 1900, 05, 15),
					teacher1, room1, term2, 3);
			Course course1 = new Course(10102, 201, 35, new Date(term.getYear() - 1900, 8, 15), new Date(term.getYear() - 1900, 11, 20),
					teacher1, room1, term, 3);
			Course course2 = new Course(10103, 202, 35, new Date(term.getYear() - 1900, 8, 15), new Date(term.getYear() - 1900, 11, 20),
					teacher1, room1, term, 3);
			Course course4 = new Course(10104, 301, 35, new Date(term.getYear() - 1900, 8, 15), new Date(term.getYear() - 1900, 11, 20),
					teacher1, room1, term, 3);
			Course course3 = new Course(10105, 302, 1, new Date(term2.getYear() - 1900 - 1, 12, 15), new Date(term2.getYear() - 1900, 05, 15),
					teacher1, room1, term2, 3);
			Course cs72 = new Course(10106, 302, 1, new Date(term2.getYear() - 1900 - 1, 12, 15), new Date(term2.getYear() - 1900, 05, 15),
					teacher1, room2, term2, 3);
			Course course5 = new Course(10107, 401, 35, new Date(term2.getYear() - 1900 - 1, 12, 15), new Date(term2.getYear() - 1900, 05, 15),
					teacher1, room1, term2, 3);
			
			cs79.setSubject(comsc79);
			course1.setSubject(subject);
			course2.setSubject(subject);
			course3.setSubject(subject2);
			course4.setSubject(subject2);
			course5.setSubject(subject3);
			cs72.setSubject(comsc72);
			courseRepository.save(course1);
			
			courseRepository.save(course3);
			courseRepository.save(course2);
			courseRepository.save(course4);
			courseRepository.save(course5);
			courseRepository.save(cs72);
			courseRepository.save(cs79);
			// Course1 and book list

			Time startTime1 = new Time(13, 45, 00);
			Time endTime1 = new Time(15, 00, 00);

			Time startTime2 = new Time(15, 15, 00);
			Time endTime2 = new Time(16, 30, 00);

			Time startTime3 = new Time(17, 00, 00);
			Time endTime3 = new Time(18, 15, 00);

			List<Date> dates = createSchedule(15, 9, 2022);

			Date day1 = new Date(122, 8, 16);
			Date day2 = new Date(122, 8, 18);

			Date day3 = new Date(121, 12, 25);
			Date day4 = new Date(122, 4, 27);

			List<Classroom> calendars1 = new ArrayList<Classroom>();
			List<Classroom> calendars2 = new ArrayList<Classroom>();
			List<Classroom> calendars3 = new ArrayList<Classroom>();
			List<Classroom> calendars4 = new ArrayList<Classroom>();
			List<Classroom> calendars5 = new ArrayList<Classroom>();
			List<Classroom> calendars6 = new ArrayList<Classroom>();
			List<Classroom> calendars7 = new ArrayList<Classroom>();
			Classroom calendar3 = new Classroom(29, startTime2, endTime2, day1, course2);
			Classroom calendar4 = new Classroom(30, startTime2, endTime2, day2, course2);

			Classroom calendar5 = new Classroom(31, startTime2, endTime2, day3, course3);
			Classroom calendar6 = new Classroom(32, startTime2, endTime2, day4, course3);

			Classroom calendar7 = new Classroom(33, startTime3, endTime3, day1, course4);
			Classroom calendar8 = new Classroom(34, startTime3, endTime3, day2, course4);

			Classroom calendar9 = new Classroom(35, startTime3, endTime3, day1, course5);
			Classroom calendar10 = new Classroom(36, startTime3, endTime3, day2, course5);
			
			Classroom calendar11 = new Classroom(37, startTime2, endTime2, day3, cs72);
			Classroom calendar12 = new Classroom(38, startTime2, endTime2, day4, cs72);
			
			Classroom calendar13 = new Classroom(39, startTime1, endTime1, day3, cs79);
			Classroom calendar14 = new Classroom(40, startTime1, endTime1, day4, cs79);

			for (int i = 0; i < 29; i++) {
				Classroom period = new Classroom(startTime1, endTime1, dates.get(i), course1);
				calendars1.add(period);
			}

			calendars2.add(calendar3);
			calendars2.add(calendar4);
			calendars3.add(calendar5);
			calendars3.add(calendar6);
			calendars4.add(calendar7);
			calendars4.add(calendar8);
			calendars5.add(calendar9);
			calendars5.add(calendar10);
			calendars6.add(calendar11);
			calendars6.add(calendar12);
			calendars7.add(calendar13);
			calendars7.add(calendar14);
			classroomRepository.saveAll(calendars1);
			classroomRepository.saveAll(calendars2);
			classroomRepository.saveAll(calendars3);
			classroomRepository.saveAll(calendars4);
			classroomRepository.saveAll(calendars5);
			classroomRepository.saveAll(calendars6);
			classroomRepository.saveAll(calendars7);
		} catch (Exception e) {
			logger.error("User already created");
		}

	}

	public static List<Date> createSchedule(int startDay, int month, int year) {

		int firstDay = startDay;

		int b = 2;
		int firstMonth = month - 1;
		int y = year - 1900;
		int start = 0;
		List<Date> dates = new ArrayList<Date>();
		Date day1 = new Date(y, firstMonth, firstDay);
		dates.add(day1);
		for (int i = 0; i < 24; i++) {

			Date even = new Date(y, firstMonth, firstDay + b);
			start++;
			dates.add(even);
			if (start == 1) {
				for (int j = 0; j < 18; j += 5) {
					Date odd = new Date(y, even.getMonth(), even.getDate() + 5 + j);
					dates.add(odd);
					firstMonth = odd.getMonth();
					start = 0;
					firstDay = odd.getDate();
					y = odd.getYear();
					break;
				}
			} else {
			}
		}
		return dates;

	}

}