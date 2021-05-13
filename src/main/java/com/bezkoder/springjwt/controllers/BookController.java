package com.bezkoder.springjwt.controllers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.BookDto;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.security.service.BookService;
import com.bezkoder.springjwt.security.service.CourseService;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
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

	@GetMapping("getRequiredBooksByCourse/{regId}")
	public ResponseEntity<?> getRequiredBooksByCourse(@PathVariable Integer regId){
		Course course = courseService.findCourseById(regId);
		List<Books> books = course.getBookList();
		books.forEach(e -> {
			String img = null;
		    try {
		        img = Files.readString(Path.of("src/main/resources/static/image/book/" + e.getId()));
		        e.setImageUrl(img);
		    } catch (IOException err) {
		        err.printStackTrace();
		    }
		});
		return ResponseEntity.ok(books);
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
