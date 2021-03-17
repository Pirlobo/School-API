package com.bezkoder.springjwt.models;
import java.sql.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "calendar")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Calendar {
	@Id
	private Integer id;
	

	private Date defaultDate;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar(Integer id, Date defaultDate) {
		super();
		this.id = id;
		this.defaultDate = defaultDate;
	}


	public Date getDefaultDate() {
		return defaultDate;
	}


	public void setDefaultDate(Date defaultDate) {
		this.defaultDate = defaultDate;
	}


	public Calendar() {
		super();
	}
	
	
}
