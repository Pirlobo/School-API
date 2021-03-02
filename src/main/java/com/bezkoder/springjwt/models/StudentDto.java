package com.bezkoder.springjwt.models;

public class StudentDto {

	private String email;
	
	private String username;
	
	private String code;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public StudentDto(String code,String email, String username) {
		super();
		this.code = code;
		this.email = email;
		this.username = username;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	
	
}
