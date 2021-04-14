package com.bezkoder.springjwt.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
//@Table(name = "teacher")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher {

	@Id
	// @GeneratedValue
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
	private List<Course> courses = new ArrayList<Course>();

	public Teacher(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;

	}

	public Teacher(Integer id, String name, List<Course> courses) {
		super();
		this.id = id;
		this.name = name;
		this.courses = courses;
	}

	public Teacher() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

}
