package com.bezkoder.springjwt.models;

import java.sql.Date;

import java.sql.Time;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;
import org.hibernate.annotations.Parameter;
@Entity
@Table(name = "classroom")
public class Classroom {

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "time_in")
	private Time startTime;
	
	@Column(name = "time_out")
	private Time endTime;
	
	private Date date;
	
    @ManyToOne
    private Course course;
    
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	
	public Classroom(Integer id, Time startTime, Time endTime, Date date, Course course) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.date = date;
		this.course = course;
		
	}

	public Classroom() {
		super();
	}
	
	public Classroom(Time startTime, Time endTime, Date date, Course course) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.date = date;
		this.course = course;
		
	}

    
    
    
    
    
    
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classroom_generator")
//	@GenericGenerator(name = "classroom_generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
//			//@Parameter(name = "sequence_name", value = "user_sequence"),
//	        @Parameter(name = "initial_value", value = "4"),
//	        @Parameter(name = "increment_size", value = "2")
//	} )
    
    
    
    
}
