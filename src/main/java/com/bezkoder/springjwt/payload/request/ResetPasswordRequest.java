package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.bezkoder.springjwt.models.PasswordResetToken;

public class ResetPasswordRequest {
	
	@Size(min = 6, max = 6, message 
		      = "6 digits")
	private String code;
	
	@Email
	private String email;
	
	@Size(min = 6, max = 40, message 
		      = "About Me must be between 6 and 40 characters")
	private String password ;
	
	@Size(min = 6, max = 40, message
		      = "About Me must be between 6 and 40 characters")
	private String password2;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	

	public ResetPasswordRequest(@Email String code,
			@Size(min = 6, max = 40, message = "About Me must be between 6 and 40 characters") String password, String email,
			@Size(min = 6, max = 40, message = "About Me must be between 6 and 40 characters") String password2) {
		super();
		this.code = code;
		this.email = email;
		this.password = password;
		this.password2 = password2;
	}

	public ResetPasswordRequest() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	
}
