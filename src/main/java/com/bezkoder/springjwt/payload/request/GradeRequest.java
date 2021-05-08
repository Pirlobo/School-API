package com.bezkoder.springjwt.payload.request;

public class GradeRequest {

	private Integer regId;

	private String userName;

	// current grade
	private Character letter;

	private Double percentage;
	
	// final grade
	private Character finalGrade;

	public Character getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(Character finalGrade) {
		this.finalGrade = finalGrade;
	}

	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Character getLetter() {
		return letter;
	}

	public void setLetter(Character letter) {
		this.letter = letter;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public GradeRequest(Integer regId, String userName, Character letter, Double percentage, Character finalGrade) {
		super();
		this.regId = regId;
		this.userName = userName;
		this.letter = letter;
		this.percentage = percentage;
		this.finalGrade = finalGrade;
	}
}
