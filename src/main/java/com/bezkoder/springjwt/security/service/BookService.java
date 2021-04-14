package com.bezkoder.springjwt.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.book.BookItems;
import com.bezkoder.springjwt.book.Books;
import com.bezkoder.springjwt.dto.BookDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.BookRepository;
import com.bezkoder.springjwt.persistence.CourseRepository;
import com.bezkoder.springjwt.persistence.UserRepository;


@Service
public class BookService implements IBookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	//@Cacheable(value = "bookCache")
	public Set<Books> getBookByTitleOrAuthor(String argument) {
		return bookRepository.findByTitleOrAuthor(argument);
	}

	@Override
	public List<BookItems> findBookItems(Books book) {
		List<BookItems>  bookItems = book.getBookItems();
		return bookItems;
	}

	@Override
	public Books findById(Integer id) {
		Books bookItem = bookRepository.findById(id).orElse(null);
		return bookItem;
	}

	@Override
	public Map<String, List<BookDto>> getResultMap() {
		Map<String, List<BookDto>> resultMap  = new HashMap<String, List<BookDto>>();
		
		User user  = userService.getCurrentLoggedUser();
		
		List<Course> courses = userService.getYourClasses(user);
		
		
//        courses.stream().map(course -> course.getTeacher().getName().equals("a")).collect(Collectors.toList());
//        courses.stream().filter(course -> course.getTeacher().getName().equals("a") ).collect(Collectors.toList());

		courses.forEach(course ->  {
			String subjectCode = course.getSubject().getSubjectCode().toString();
			List<BookDto> bookDtos = new ArrayList<>();
			List<Books> books = course.getBooks();
			books.forEach(book -> {
				BookDto bookDto = new BookDto(book.getId(), book.getISBN(), book.getTitle(), book.getPublisher(), book.getLanguage(), book.getNumOfPages(), book.getBookItems(), book.getAuthorList(),subjectCode);
				bookDtos.add(bookDto);
			});
			resultMap.put(subjectCode, bookDtos);
		});
		
		
		return resultMap;
	}
	

}