package com.bezkoder.springjwt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class BookExistsException extends Exception{
	private static final long serialVersionUID = 1L;

	public BookExistsException(String errorMessage) {
		super(errorMessage);
	}

}


