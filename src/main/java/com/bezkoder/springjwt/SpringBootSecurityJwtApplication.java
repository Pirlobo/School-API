package com.bezkoder.springjwt;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import com.bezkoder.springjwt.book.Authors;
import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.book.Format;
import com.bezkoder.springjwt.models.Building;
import com.bezkoder.springjwt.models.Calendar;
import com.bezkoder.springjwt.models.Classroom;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.Room;
import com.bezkoder.springjwt.models.Semester;
import com.bezkoder.springjwt.models.Subject;
import com.bezkoder.springjwt.models.SubjectCode;
import com.bezkoder.springjwt.models.SubjectName;
import com.bezkoder.springjwt.models.Teacher;
import com.bezkoder.springjwt.models.Term;
import com.bezkoder.springjwt.persistence.BookItemsRepository;
import com.bezkoder.springjwt.persistence.BookRepository;
import com.bezkoder.springjwt.persistence.BuildingRepository;
import com.bezkoder.springjwt.persistence.CalendarRepository;
import com.bezkoder.springjwt.persistence.ClassroomRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.RoleRepository;
import com.bezkoder.springjwt.persistence.RoomRepository;
import com.bezkoder.springjwt.persistence.SubjectRepository;
import com.bezkoder.springjwt.persistence.TeacherRepository;
import com.bezkoder.springjwt.persistence.TermRepository;

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
	private CalendarRepository calendarRepository;

	@Autowired
	private TermRepository termRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

	@PostConstruct
	public void init() {

		List<Books> books = new ArrayList<Books>();

		Books book1 = new Books(1, "978-3-16-148410-0", "Java 2", "Anh T Nguyen", "English", 100);
		Books book2 = new Books(2, " 978-1-4028-9462-6", "Java 1", "Anh T Nguyen", "English", 200);

		List<BookItems> bookItems = new ArrayList<BookItems>();
		BookItems bookItem1 = new BookItems(1234, 150.0, 300.0, true, Format.Paper_Pack);
		BookItems bookItem2 = new BookItems(5678, 200.0, 400.0, true, Format.Hard_Cover);

		bookItems.add(bookItem1);
		bookItems.add(bookItem2);
		bookItemsRepository.saveAll(bookItems);

		Authors author1 = new Authors(1, "Phan Manh Quynh", "Love");
		Authors author2 = new Authors(2, "Pham Hong Phuoc", "Science Fiction");

		books.add(book1);
		books.add(book2);

		book1.addAuthor(author1);
		book1.addAuthor(author2);

		book2.addAuthor(author1);

		book1.addBookItem(bookItem1);
		book1.addBookItem(bookItem2);
		bookRepository.save(book1);
		bookRepository.save(book2);

		// persisting default Date
		Calendar calendar = new Calendar(1, new Date(121, 12, 25));
		calendarRepository.save(calendar);

		// new an object of Teacher

		Building building1 = new Building(1, "MS");
		List<Room> rooms = new ArrayList<Room>();
		Room room1 = new Room(1, "MS302", building1);
		Room room2 = new Room(2, "MS303", building1);

		rooms.add(room1);
		rooms.add(room2);

		buildingRepository.save(building1);
		roomRepository.saveAll(rooms);
		Teacher teacher1 = new Teacher(1, "Hendry Estrada");
		teacherRepository.save(teacher1);

		Date startDay = new Date(120, 8, 15);
		Date endDay = new Date(120, 11, 20);

		Date startDay1 = new Date(119, 12, 25);
		Date endDay1 = new Date(120, 4, 27);

		// new Subject

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

		Term term = new Term(1, Semester.Fall, 2022);
		Term term2 = new Term(2, Semester.Spring, 2022);
		termRepository.save(term);
		termRepository.save(term2);

		// new courses with fk_teacher
		Course course1 = new Course(10101, 201, 35, new Date(2022 - 1900, 8, 15), new Date(2022 - 1900, 11, 20),
				teacher1, room1, term);
		Course course2 = new Course(10102, 202, 35, new Date(2022 - 1900, 8, 15), new Date(2022 - 1900, 11, 20),
				teacher1, room1, term);
		Course course4 = new Course(10103, 301, 35, new Date(2022 - 1900, 8, 15), new Date(2022 - 1900, 11, 20),
				teacher1, room1, term);
		Course course3 = new Course(10104, 302, 1, new Date(2021 - 1900, 12, 15), new Date(2022 - 1900, 05, 15),
				teacher1, room1, term2);
		Course course5 = new Course(10105, 401, 35, new Date(2021 - 1900, 12, 15), new Date(2022 - 1900, 05, 15),
				teacher1, room1, term2);
		course1.setSubject(subject);
		course2.setSubject(subject);
		course3.setSubject(subject2);
		course4.setSubject(subject2);
		course5.setSubject(subject3);
		course1.getBooks().add(book1);
		course1.getBooks().add(book2);
		courseRepository.save(course1);
		course3.getBooks().add(book2);

		courseRepository.save(course3);
		courseRepository.save(course2);
		courseRepository.save(course4);
		courseRepository.save(course5);
		// Course1 and book list

		Time startTime1 = new Time(13, 45, 00);
		Time endTime1 = new Time(15, 00, 00);

		Time startTime2 = new Time(15, 15, 00);
		Time endTime2 = new Time(16, 30, 00);

		Time startTime3 = new Time(17, 00, 00);
		Time endTime3 = new Time(18, 15, 00);

		List<Date> dates = createSchedule(15, 9, 2020);

		Date day1 = new Date(120, 8, 16);
		Date day2 = new Date(120, 8, 18);

		Date day3 = new Date(121, 12, 25);
		Date day4 = new Date(121, 4, 27);

		List<Classroom> calendars1 = new ArrayList<Classroom>();
		List<Classroom> calendars2 = new ArrayList<Classroom>();
		List<Classroom> calendars3 = new ArrayList<Classroom>();
		List<Classroom> calendars4 = new ArrayList<Classroom>();
		List<Classroom> calendars5 = new ArrayList<Classroom>();
		Classroom calendar3 = new Classroom(29, startTime2, endTime2, day1, course2);
		Classroom calendar4 = new Classroom(30, startTime2, endTime2, day2, course2);

		Classroom calendar5 = new Classroom(31, startTime2, endTime2, day3, course3);
		Classroom calendar6 = new Classroom(32, startTime2, endTime2, day4, course3);

		Classroom calendar7 = new Classroom(33, startTime3, endTime3, day1, course4);
		Classroom calendar8 = new Classroom(34, startTime3, endTime3, day2, course4);

		Classroom calendar9 = new Classroom(35, startTime3, endTime3, day1, course5);
		Classroom calendar10 = new Classroom(36, startTime3, endTime3, day2, course5);

		int a = 0;
		for (int i = 0; i < 29; i++) {
			// int b = a++;
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
		classroomRepository.saveAll(calendars1);
		classroomRepository.saveAll(calendars2);
		classroomRepository.saveAll(calendars3);
		classroomRepository.saveAll(calendars4);
		classroomRepository.saveAll(calendars5);
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
					Date odd = new Date(120, even.getMonth(), even.getDate() + 5 + j);
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