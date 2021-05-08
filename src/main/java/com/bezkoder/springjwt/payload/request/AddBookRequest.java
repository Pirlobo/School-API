package com.bezkoder.springjwt.payload.request;

import com.sun.istack.NotNull;

public class AddBookRequest {
	
	@NotNull
	private String imageBase64Value;
	
	@NotNull
	private Integer regId;

	@NotNull
	private String isbn;
	
	public Integer getRegId() {
		return regId;
	}

	public String getImageBase64Value() {
		return imageBase64Value;
	}

	public void setImageBase64Value(String imageBase64Value) {
		this.imageBase64Value = imageBase64Value;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	@NotNull
	private String title;

	@NotNull
	private String publisher;
	
	@NotNull
	private String listOfAuthors;

	@NotNull
	private String imageUrl;

	public String getIsbn() {
		return isbn;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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

	public AddBookRequest(Integer regId, String isbn, String title, String publisher, String listOfAuthors, String imageUrl) {
		super();
		this.regId = regId;
		this.isbn = isbn;
		this.title = title;
		this.publisher = publisher;
		this.listOfAuthors = listOfAuthors;
		this.imageUrl = imageUrl;
	}

	
	
	

}
