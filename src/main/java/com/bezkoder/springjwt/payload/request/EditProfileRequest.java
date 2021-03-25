package com.bezkoder.springjwt.payload.request;

public class EditProfileRequest {
	
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EditProfileRequest(String email) {
		super();
		this.email = email;
	}

	public EditProfileRequest() {
		super();
	}
	
	

}
