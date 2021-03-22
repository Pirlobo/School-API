package com.bezkoder.springjwt.dto;

public class CourseStudentDto {

	private String userName;
	
	private String email;
	
	private String registerStatus;
	
	private String courseTitle;
	
	private String description;
	
	private Integer wailistedRank;
	
	private Integer capacity;
	
	private Integer available;
	
	

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

	public Integer getWailistedRank() {
		return wailistedRank;
	}

	public void setWailistedRank(Integer wailistedRank) {
		this.wailistedRank = wailistedRank;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CourseStudentDto(String userName, String email, String registerStatus, String courseTitle,
			String description, Integer wailistedRank, Integer capacity, Integer available) {
		super();
		this.userName = userName;
		this.email = email;
		this.registerStatus = registerStatus;
		this.courseTitle = courseTitle;
		this.description = description;
		this.wailistedRank = wailistedRank;
		this.capacity = capacity;
		this.available = available;
	}


	
}
