package com.bezkoder.springjwt.security.service;

import java.awt.print.Book;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.payload.request.AddBookRequest;
import com.bezkoder.springjwt.persistence.BookRepository;

@Service
public class BookService implements IBookService {
	private static final Logger LOGGER=LoggerFactory.getLogger(BookService.class);
	// BookRepository instance will be created by @Repository
	// Now we have bookRepo object
	// BookService instance will be created and has bookRepo as a dependency which is set implicitly via setter method
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired CourseService courseService;
	
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
	public void save(AddBookRequest addBookRequest) {
		Course course = courseService.findCourseById(addBookRequest.getRegId());
		String bookImage = addBookRequest.getImageBase64Value();
        Books book = new Books(addBookRequest.getIsbn(), addBookRequest.getTitle(), addBookRequest.getPublisher(), 
        		addBookRequest.getListOfAuthors(), bookImage, course);
        Books savedBook = bookRepository.save(book);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("src/main/resources/static/image/book/" + savedBook.getId()));
            writer.write(bookImage);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}