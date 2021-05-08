package com.bezkoder.springjwt.dto;
import com.sun.istack.NotNull;

public class BookDto {
	
	@NotNull
	private String subjectCode;
	
	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	@NotNull
	private String ISBN;

	@NotNull
	private String title;

	@NotNull
	private String publisher;

	@NotNull
	private String listOfAuthors;
	
	private String imageUrl;
	
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
	public String getListOfAuthors() {
		return listOfAuthors;
	}

	public void setListOfAuthors(String listOfAuthors) {
		this.listOfAuthors = listOfAuthors;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BookDto(String subjectCode, String iSBN, String title, String publisher, String listOfAuthors,
			String imageUrl) {
		super();
		this.subjectCode = subjectCode;
		ISBN = iSBN;
		this.title = title;
		this.publisher = publisher;
		this.listOfAuthors = listOfAuthors;
		this.imageUrl = imageUrl;
	}

	
}