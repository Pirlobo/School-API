package com.bezkoder.springjwt.dto;

import java.sql.Date;
import java.sql.Time;

public class CourseDto {
	private Integer regId;

	private Integer section;

	private Integer available;
	
	private Integer capacity;

	private Date startDay;

	private Date endDay;
	
	private Integer waitlist;
	
	private String title;
	
	private String room;
	
	private String instructor;
	
	private String from;
	
	private String to;
	
	private String prerequisite;
	
	private String term;
	
	private String courseDescription;
	
	private String year;
	
	String registerStatus;
	
	private Integer rank; 
	

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
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

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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

	public Integer getWaitlist() {
		return waitlist;
	}

	public void setWaitlist(Integer waitlist) {
		this.waitlist = waitlist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(String prerequisite) {
		this.prerequisite = prerequisite;
	}

	public CourseDto(Integer regId, Integer section, Integer available, Integer capacity, Date startDay, Date endDay,
			Integer waitlist, String title, String room, String instructor, String from, String to, String prerequisite, String term, String courseDescription, String year, String registerStatus, Integer rank) {
		super();
		this.regId = regId;
		this.section = section;
		this.available = available;
		this.capacity = capacity;
		this.startDay = startDay;
		this.endDay = endDay;
		this.waitlist = waitlist;
		this.title = title;
		this.room = room;
		this.instructor = instructor;
		this.from = from;
		this.to = to;
		this.prerequisite = prerequisite;
		this.term = term;
		this.courseDescription = courseDescription;
		this.year = year;
		this.registerStatus = registerStatus;
		this.rank = rank;
	}
	public CourseDto(Integer regId, Integer section, Integer available, Integer capacity, Date startDay, Date endDay,
			Integer waitlist, String title, String room, String instructor, String from, String to, String prerequisite, String term, String courseDescription, String year, String registerStatus) {
		super();
		this.regId = regId;
		this.section = section;
		this.available = available;
		this.capacity = capacity;
		this.startDay = startDay;
		this.endDay = endDay;
		this.waitlist = waitlist;
		this.title = title;
		this.room = room;
		this.instructor = instructor;
		this.from = from;
		this.to = to;
		this.prerequisite = prerequisite;
		this.term = term;
		this.courseDescription = courseDescription;
		this.year = year;
		this.registerStatus = registerStatus;
		
	}
	
	public CourseDto(Integer regId, Integer section, Integer available, Integer capacity, Date startDay, Date endDay,
			Integer waitlist, String title, String room, String instructor, String from, String to, String prerequisite, String term, String courseDescription, String year) {
		super();
		this.regId = regId;
		this.section = section;
		this.available = available;
		this.capacity = capacity;
		this.startDay = startDay;
		this.endDay = endDay;
		this.waitlist = waitlist;
		this.title = title;
		this.room = room;
		this.instructor = instructor;
		this.from = from;
		this.to = to;
		this.prerequisite = prerequisite;
		this.term = term;
		this.courseDescription = courseDescription;
		this.year = year;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	
	
	
	

}
