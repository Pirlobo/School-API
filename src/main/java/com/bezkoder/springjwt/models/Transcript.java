package com.bezkoder.springjwt.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Transcript {

	@Id
	@GeneratedValue
	private Integer id;
	
	@OneToOne
	@JsonIgnore
	User user;
	
	private Integer totalEarnedCredits;
	
	private Integer totalGradePoints;
	
	private Double cumulativegpa;

	public Double getCumulativegpa() {
		return cumulativegpa;
	}

	public Transcript() {
		super();
	}

	public void setCumulativegpa(Double cumulativegpa) {
		this.cumulativegpa = cumulativegpa;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getTotalEarnedCredits() {
		return totalEarnedCredits;
	}

	public void setTotalEarnedCredits(Integer totalEarnedCredits) {
		this.totalEarnedCredits = totalEarnedCredits;
	}

	public Integer getTotalGradePoints() {
		return totalGradePoints;
	}

	public void setTotalGradePoints(Integer totalGradePoints) {
		this.totalGradePoints = totalGradePoints;
	}

	public Double cumulativegpa() {
		return cumulativegpa;
	}

	public void setCommutativeGPA(Double cumulativegpa) {
		this.cumulativegpa = cumulativegpa;
	}

	public Transcript(User user, Integer totalEarnedCredits, Integer totalGradePoints, Double cumulativegpa) {
		super();
		this.user = user;
		this.totalEarnedCredits = totalEarnedCredits;
		this.totalGradePoints = totalGradePoints;
		this.cumulativegpa = cumulativegpa;
	}
	
	
	
	
}
