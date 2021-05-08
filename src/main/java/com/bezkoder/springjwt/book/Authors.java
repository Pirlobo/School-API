package com.bezkoder.springjwt.book;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
@Cacheable
@Table(name = "author")
public class Authors {
	@Id
	// @GeneratedValue
	private Integer id;

	public Authors(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "books_authors", joinColumns = { @JoinColumn(name = "fk_author") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_book") })
	@JsonIgnore
	private List<Books> bookList = new ArrayList<Books>();

	@NotNull
	private String name;

	@NotNull
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Authors() {
		super();
	}

	public List<Books> getBookList() {
		return bookList;
	}

	public void setBookList(List<Books> bookList) {
		this.bookList = bookList;
	}

}
