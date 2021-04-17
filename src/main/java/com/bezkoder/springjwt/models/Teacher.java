package com.bezkoder.springjwt.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name = "teacher")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher {

	@Id
	// @GeneratedValue
	private Integer id;

	private String name;

	@OneToOne
	@JsonIgnore
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
	private List<Course> courses = new ArrayList<Course>();

	

	public Teacher(Integer id, User user) {
		super();
		this.id = id;
		if (user != null) {
			this.name = user.getUsername();
		} else {
			this.name = "Not planned yet";
		}
		this.user = user;
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
