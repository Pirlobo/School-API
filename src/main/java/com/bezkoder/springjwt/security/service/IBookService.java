package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.BookDto;
import com.bezkoder.springjwt.models.Course;

@Service
public interface IBookService {

	
	// find a list of books by title or author's name of the books 
	Set<Books> getBookByTitleOrAuthor(String argument);
       
	
	// find all book items of a book
	List<BookItems> findBookItems(Books book);
	
	// find books by id (isbn)
	Books findById(Integer id);
	
	public Map<String, List<BookDto>> getResultMap();
		
	
	
}
