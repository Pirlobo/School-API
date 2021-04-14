package com.bezkoder.springjwt.controllers;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.book.Orders;
import com.bezkoder.springjwt.dto.BookDto;
import com.bezkoder.springjwt.dto.BookItemDto;
import com.bezkoder.springjwt.exceptions.NullExceptionHandler;
import com.bezkoder.springjwt.exceptions.ResourceNotFoundException;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.persistence.BookRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.security.service.BookService;
import com.bezkoder.springjwt.security.service.CourseService;
import com.bezkoder.springjwt.security.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;
	
	
	
	@RequestMapping("/searchBookByTitleOrAuthor")
	public ResponseEntity<?> getBookListByAuthor( @RequestParam("keyword") String keyword) {
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
		Set<Books> bookList = courseService.getAllRequiredBooks();
		if (bookList.size() == 0) {
			throw new ResourceNotFoundException("Cant find any book");
		}
		// 	Key : Course's title
		// Value : List of Books required for the course/s
		Map<String, List<BookDto>> resultMap = bookService.getResultMap();
		return ResponseEntity.ok(resultMap);
	}
	

	@RequestMapping("/moreDetails/{id}")
	public ResponseEntity<?> getBookItem(@PathVariable Integer id) {
		Books book = bookService.findById(id);
		List<BookItems> bookItems = bookService.findBookItems(book);
		return ResponseEntity.ok(bookItems);
	}

	@GetMapping("/buyBook")
	public ResponseEntity<?> buyBook() {

		List<BookItemDto> bookItemDto = new ArrayList<BookItemDto>();
		BookItemDto bookItemDto1 = new BookItemDto(1234, false, true, 1);
		BookItemDto bookItemDto2 = new BookItemDto(5678, false, true, 3);
		bookItemDto.add(bookItemDto1);
		bookItemDto.add(bookItemDto2);

		Orders orders = userService.placeOrder(bookItemDto);

		return ResponseEntity.ok(orders);
	}

}

