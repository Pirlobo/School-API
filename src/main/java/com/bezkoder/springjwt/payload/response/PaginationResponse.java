package com.bezkoder.springjwt.payload.response;

import java.util.List;

import com.bezkoder.springjwt.dto.CourseDto;
import com.bezkoder.springjwt.models.Course;

public class PaginationResponse {
	
	private int totalPages;
	
	private long totalElements; 
	
	private String orderBy;
	
	private List<CourseDto> dtoCourses;

	public int getTotalPages() {
		return totalPages;
	}



	public List<CourseDto> getDtoCourses() {
		return dtoCourses;
	}



	public void setDtoCourses(List<CourseDto> dtoCourses) {
		this.dtoCourses = dtoCourses;
	}



	public long getTotalElements() {
		return totalElements;
	}



	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}



	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public String getOrderBy() {
		return orderBy;
	}



	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public PaginationResponse(int totalPages, long totalElements, String orderBy,
			List<CourseDto> dtoCourses) {
		super();
		this.totalPages = totalPages;
		this.totalElements = totalElements;
		this.orderBy = orderBy;
		this.dtoCourses = dtoCourses;
	}


}
