package com.bezkoder.springjwt.security.service;

import java.io.IOException;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.payload.request.AddBookRequest;

@Service
public interface IBookService {

	// find a list of books by title or author's name of the books
	Set<Books> getBookByTitleOrAuthor(String argument);

	// find books by id (isbn)
	Books findById(String isbn);
	
	void save(AddBookRequest book);

//	public Map<String, List<BookDto>> getResultMap();

}
