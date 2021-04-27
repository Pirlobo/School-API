package com.bezkoder.springjwt.security.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.payload.response.PaginationResponse;

@Service
public class PaginationService {

	@Autowired
	private CourseService courseService;

	public PaginationResponse getPagination(int pageNo, String search, String sortField, String sortDir) {
		int pageSize = 5;
		int year = Calendar.getInstance().get(Calendar.YEAR);
		Page<Course> page = courseService.findPaginatedByTitle(year + 1, search, pageNo, pageSize, sortField, sortDir);
//		Page<Course> page = courseService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<CourseDto> courseDtos = courseService.convert(page.getContent());
		PaginationResponse paginationResponse = new PaginationResponse(page.getTotalPages(), page.getTotalElements(),
				sortDir.equals("asc") ? "desc" : "asc", courseDtos);
		return paginationResponse;
	}
}
