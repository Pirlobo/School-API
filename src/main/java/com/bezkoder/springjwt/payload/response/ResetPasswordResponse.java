package com.bezkoder.springjwt.payload.response;

public class ResetPasswordResponse {
	
	private String resetPasswordToken;

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public ResetPasswordResponse(String resetPasswordToken) {
		super();
		this.resetPasswordToken = resetPasswordToken;
	}
	
	
	
}
