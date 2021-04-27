package com.bezkoder.springjwt.security.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.persistence.BookRepository;

@Service
public class BookService implements IBookService {

	// BookRepository instance will be created by @Repository
	// Now we have bookRepo object
	// BookService instance will be created and has bookRepo as a dependency which is set implicitly via setter method
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	// @Cacheable(value = "bookCache")
	public Set<Books> getBookByTitleOrAuthor(String argument) {
		return bookRepository.findByTitleOrAuthor(argument);
	}

	@Override
	public Books findById(String isbn) {
		Books bookItem = bookRepository.findById(isbn).orElse(null);
		return bookItem;
	}

	@Override
	public void save(Books book) {
		bookRepository.save(book);
	}

}