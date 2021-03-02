package com.bezkoder.springjwt.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import groovy.transform.Internal;

@Entity
@Table(name = "term")
public class Term {

	@Id
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private Semester semester;
	
	private Integer year;
	
	@OneToMany(mappedBy = "term")
	List<Course> courses = new ArrayList<Course>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public Term(Integer id, Semester semester, Integer year) {
		super();
		this.id = id;
		this.semester = semester;
		this.year = year;
	}

	public Term() {
		super();
	}
	
	
}
