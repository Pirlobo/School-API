package com.bezkoder.springjwt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NullExceptionHandler extends NullPointerException {

	private static final long serialVersionUID = 1L;

	public NullExceptionHandler(String message) {
		super(message);
	}
}