package com.bezkoder.springjwt.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
public class Assignment {

	public Assignment() {
		super();
	}

	@GeneratedValue
	@Id
	private Integer id;
	
	@NotNull
	private String description;
	
	@NotNull
	private Integer points;
	
	@NotNull
	private Date date;
	
	@NotNull
	private String name;

	@NotNull
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Lob
	private byte[] data;

	@ManyToOne
	@JsonIgnore
	private Course course;
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Assignment(String description, Integer points, Date date, String name, String type, byte[] data,
			Course course) {
		super();
		this.description = description;
		this.points = points;
		this.date = date;
		this.name = name;
		this.type = type;
		this.data = data;
		this.course = course;
	}


	
	
}
