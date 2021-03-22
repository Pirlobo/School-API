package com.bezkoder.springjwt.models;

import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bezkoder.springjwt.book.Books;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import antlr.debug.NewLineEvent;

@Entity
@Table(name = "course")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course {

	@Id
	// @GeneratedValue
	@Column(unique = true, name = "id")
	private Integer regId;

	private Integer section;

	private Integer available;
	
	private Integer capacity;

	private Date startDay;

	private Date endDay;
	
	@ManyToOne
	@JsonIgnore
	private Term term;
	
	
	

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name = "course_books",
//	joinColumns = { @JoinColumn(name = "fk_course") },
//	inverseJoinColumns = { @JoinColumn(name = "fk_book") })
	@JsonIgnore
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private List<Books> books = new ArrayList<Books>();



	public void setBooks(List<Books> books) {
		this.books = books;
	}
	
	public List<Books> getBooks() {
		return books;
	}

	public void setBookList(List<Books> books) {
		this.books = books;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

//	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Books> books = new ArrayList<Books>();
	
	@ManyToOne 
	private Subject subject;
	
	
	private Integer waitlist;
	

	public Integer getWaitlist() {
		return waitlist;
	}

	public void setWaitlist(Integer waitlist) {
		this.waitlist = waitlist;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	@ManyToOne
	@JsonIgnore
	private Teacher teacher;

	@OneToMany(mappedBy = "course")
	@JsonIgnore
	private List<Classroom> calendars = new ArrayList<Classroom>();

	@ManyToOne
	@JoinColumn(name = "room_id", foreignKey = @javax.persistence.ForeignKey(name = "room_id_FK"))
	@JsonIgnore
	private Room room;

	public List<Classroom> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<Classroom> calendars) {
		this.calendars = calendars;
	}

//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name = "users_courses",
//	joinColumns = { @JoinColumn(name = "fk_course") },
//	inverseJoinColumns = { @JoinColumn(name = "fk_user") })
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<StudentCourse> users = new ArrayList<StudentCourse>();

	public List<StudentCourse> getUsers() {
		return users;
	}

	public Course() {
		super();
	}

	public Course(Integer regId, Integer section, Integer capacity, Date startDay, Date endDay, Teacher teacher,
			Room room, Term term) {
		super();
		this.regId = regId;
		this.section = section;
		this.available = capacity;
		this.capacity = capacity;
		this.waitlist = 0;
		this.startDay = startDay;
		this.endDay = endDay;
		this.teacher = teacher;
		this.room = room;
		this.term = term;
	}

	public Course(Integer regId, Integer section, Integer available, Date startDay, Date endDay, Teacher teacher,
			List<Classroom> calendars, List<StudentCourse> users, Term term) {
		super();
		this.regId = regId;
		this.section = section;
		this.available = available;
		this.startDay = startDay;
		this.endDay = endDay;

		this.teacher = teacher;
		this.calendars = calendars;

		this.users = users;
		this.term = term;
	}
	

	public Course(Integer regId, Integer section, Integer available, Integer capacity, Date startDay, Date endDay,
			Integer waitlist) {
		super();
		this.regId = regId;
		this.section = section;
		this.available = available;
		this.capacity = capacity;
		this.startDay = startDay;
		this.endDay = endDay;
		this.waitlist = waitlist;
	}

	public void setUsers(List<StudentCourse> users) {
		this.users = users;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Integer getRegId() {
		return regId;
	}

	public void setReg_Id(Integer regId) {
		this.regId = regId;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	

}
