package com.bezkoder.springjwt.book;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.bezkoder.springjwt.models.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

@Entity
@Table(name = "book")
public class Books {

	@Id
	// @GeneratedValue
	private Integer id;

	@NotNull
	private String ISBN;
	@NotNull
	private String title;

	@NotNull
	private String publisher;

	@NotNull
	private String language;

	@NotNull
	private Integer numOfPages;

	@ManyToMany(mappedBy = "books", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name = "course_books",
//	joinColumns = { @JoinColumn(name = "fk_book") },
//	inverseJoinColumns = { @JoinColumn(name = "fk_course") })
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonIgnore
	private List<Course> courses = new ArrayList<Course>();

	@OneToMany(fetch = FetchType.LAZY)
	private List<BookItems> bookItems = new ArrayList<BookItems>();

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "books_authors", joinColumns = { @JoinColumn(name = "fk_book") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_author") })
	@JsonIgnore
	private List<Authors> authorList = new ArrayList<Authors>();

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getNumOfPages() {
		return numOfPages;
	}

	public void setNumOfPages(Integer numOfPages) {
		this.numOfPages = numOfPages;
	}

	public Books() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Books(Integer id, String iSBN, String title, String publisher, String language, Integer numOfPages) {
		super();
		this.id = id;
		ISBN = iSBN;
		this.title = title;
		this.publisher = publisher;
		this.language = language;
		this.numOfPages = numOfPages;

	}

	public void addAuthor(Authors author) {
		authorList.add(author);
		author.getBookList().add(this);
	}

	public Books(Integer id, String iSBN, String title, String publisher, String language, Integer numOfPages,
			List<Authors> authorList) {
		super();
		this.id = id;
		ISBN = iSBN;
		this.title = title;

		this.publisher = publisher;
		this.language = language;
		this.numOfPages = numOfPages;
		this.authorList = authorList;
	}

	public List<Authors> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(List<Authors> authorList) {
		this.authorList = authorList;
	}

	public List<BookItems> getBookItems() {
		return this.bookItems;
	}

	public void setBookItems(List<BookItems> bookItems) {
		this.bookItems = bookItems;
	}

	public void addBookItem(BookItems bookItem) {
		bookItems.add(bookItem);
	}

}
