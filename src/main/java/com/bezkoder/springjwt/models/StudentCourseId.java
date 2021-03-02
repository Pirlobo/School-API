package com.bezkoder.springjwt.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StudentCourseId implements Serializable {
	
	private Long id;
	
	
	private Integer regId;

	

	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	public StudentCourseId(Long id, Integer regId) {
		super();
		this.id = id;
		this.regId = regId;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StudentCourseId() {
		super();
	}
	
	
	
}
