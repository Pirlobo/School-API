package com.bezkoder.springjwt.dto;

public class TranscriptDto {
	
	private String subjectCode;
	
	// current Grade
	private Character letter;
	
	private Double percentage;
	
	// final Grade
	private Character finalGrade;
	
	private String term;
	
	public String getSubjectCode() {
		return subjectCode;
	}


	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
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

	public Character getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(Character finalGrade) {
		this.finalGrade = finalGrade;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public TranscriptDto(String subjectCode, Character letter, Double percentage, Character finalGrade, String term) {
		super();
		this.subjectCode = subjectCode;
		this.letter = letter;
		this.percentage = percentage;
		this.finalGrade = finalGrade;
		this.term = term;
	}
	
	


}
