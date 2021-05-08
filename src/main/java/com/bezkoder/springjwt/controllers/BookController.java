package com.bezkoder.springjwt.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.BookDto;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.security.service.BookService;
import com.bezkoder.springjwt.security.service.CourseService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/book")
public class BookController {

	// please give me an instance of this class
	@Autowired
	private BookService bookService;

	@Autowired
	private CourseService courseService;

	@RequestMapping("/searchBookByTitleOrAuthor")
	public ResponseEntity<?> getBookListByAuthor(@RequestParam("keyword") String keyword) {
		Set<Books> bookSet = bookService.getBookByTitleOrAuthor(keyword);

		if (bookSet.size() == 0) {
			throw new ResourceNotFoundException("Cant find any book");
		} else {
			List<Books> bookList = new ArrayList<>();
			bookSet.forEach(e -> {
				bookList.add(e);
			});
			Collections.reverse(bookList);

			return ResponseEntity.ok(bookList);
		}

	}

	@GetMapping("/getAllRequiredBooks")
	public ResponseEntity<?> getAllRequiredBooks() {
		Set<BookDto> bookList = courseService.getAllRequiredBooks();
		if (bookList.size() == 0) {
			throw new ResourceNotFoundException("You have no materials required");
		}
		return ResponseEntity.ok(bookList);
	}

}
