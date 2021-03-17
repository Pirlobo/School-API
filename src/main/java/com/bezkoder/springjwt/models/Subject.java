package com.bezkoder.springjwt.models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "subject")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subject {

	@Id
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private SubjectName subjectName;
	
	@Enumerated(EnumType.STRING)
	private SubjectCode subjectCode;
	
	@OneToMany(mappedBy = "subject")
	@JsonIgnore
	private List<Course> courses = new ArrayList<Course>();
	
	@OneToOne
	@JsonIgnore
	private Subject prerequisite;
	
	
	public Subject getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(Subject prerequisite) {
		this.prerequisite = prerequisite;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SubjectName getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(SubjectName subjectName) {
		this.subjectName = subjectName;
	}

	public SubjectCode getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(SubjectCode subjectCode) {
		this.subjectCode = subjectCode;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public Subject() {
		super();
	}
	
	public void addCourse(Course course) {
		courses.add(course);
		course.setSubject(this);
	}

	public Subject(Integer id, SubjectName subjectName, SubjectCode subjectCode, Subject prerequisite) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.subjectCode = subjectCode;
		this.prerequisite = prerequisite;
	}
	
}
