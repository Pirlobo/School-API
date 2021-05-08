package com.bezkoder.springjwt.dto;
public class GradeDto {

	String name ;
	
	// current Grade
	Character letter;
	
	Double percentage;
	
	// final Grade
	Character finalGrade;
	
	String term;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getLetter() {
		return letter;
	}

	public void setLetter(Character letter) {
		this.letter = letter;
	}

	public Character getFinalGrade() {
		return finalGrade;
	}

	public GradeDto(String name, Character letter, Double percentage, Character finalGrade, String term) {
		super();
		this.name = name;
		this.letter = letter;
		this.percentage = percentage;
		this.finalGrade = finalGrade;
		this.term = term;
	}

	public void setFinalGrade(Character finalGrade) {
		this.finalGrade = finalGrade;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}


	
}
