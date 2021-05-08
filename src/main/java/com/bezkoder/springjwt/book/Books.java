package com.bezkoder.springjwt.book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bezkoder.springjwt.models.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
@Table(name = "book")
public class Books {

	@Id
	@GeneratedValue
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String isbn;

	@NotNull
	private String title;

	@NotNull
	private String publisher;
	
	@NotNull
	private String listOfAuthors;
	
	@Transient
	private String imageUrl;
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@ManyToOne
	@JsonIgnore
	private Course course;

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getListOfAuthors() {
		return listOfAuthors;
	}

	public void setListOfAuthors(String listOfAuthors) {
		this.listOfAuthors = listOfAuthors;
	}

//	@OneToMany(fetch = FetchType.LAZY)
//	private List<BookItems> bookItems = new ArrayList<BookItems>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Books() {
		super();
	}

	public Books(String isbn, String title, String publisher, String listOfAuthors, String imageUrl, Course course) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.publisher = publisher;
		this.listOfAuthors = listOfAuthors;
		this.imageUrl = imageUrl;
		this.course = course;
	}



}
