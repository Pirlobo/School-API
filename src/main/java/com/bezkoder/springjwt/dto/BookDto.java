package com.bezkoder.springjwt.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.bezkoder.springjwt.book.Authors;
import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.SubjectCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

public class BookDto {
	
	private Integer id;

	private String subjectCode;
	
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

	private List<BookItems> bookItems = new ArrayList<BookItems>();

	private List<Authors> authorList = new ArrayList<Authors>();

	public List<BookItems> getBookItems() {
		return bookItems;
	}

	public void setBookItems(List<BookItems> bookItems) {
		this.bookItems = bookItems;
	}

	public List<Authors> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(List<Authors> authorList) {
		this.authorList = authorList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public BookDto(Integer id, String isbn, String title, String publisher, String language, Integer numOfPages
			, List<BookItems> bookItems, List<Authors> authorList, String subjectCode
			) {
		super();
		this.id = id;
		this.ISBN = isbn;
		this.title = title;
		this.publisher = publisher;
		this.language = language;
		this.numOfPages = numOfPages;
		this.bookItems = bookItems;
		this.authorList = authorList;
		this.subjectCode = subjectCode;
	}


	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public void setNumOfPages(Integer numOfPages) {
		this.numOfPages = numOfPages;
	}

}