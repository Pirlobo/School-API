package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.bezkoder.springjwt.models.PasswordResetToken;
import com.sun.istack.NotNull;

public class ResetPasswordRequest {
	
	@Email
	private String email;
	
	@NotNull
	@Size(min = 6 , max = 6)
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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



	public ResetPasswordRequest(@Email String email, @Size(min = 6, max = 6) String code,
			@Size(min = 6, max = 40, message = "About Me must be between 6 and 40 characters") String password,
			@Size(min = 6, max = 40, message = "About Me must be between 6 and 40 characters") String password2) {
		super();
		this.email = email;
		this.code = code;
		this.password = password;
		this.password2 = password2;
	}

	public ResetPasswordRequest() {
		super();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	
}
