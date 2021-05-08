package com.bezkoder.springjwt.security.service;

import java.sql.Date;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Semester;

@Service
public interface ICalendarService {

	// get Current Time
	public Date getCurrentTime();

	// due day to return rented books
	public Date getDefaultDueDate();

	// is current time before the registry time zone to register for classes
	boolean isBeforeDefaultTime(Semester semester);
	
	// is current time after the registry time zone to register for classes
	boolean isAfterDefaultTime(Semester semester);
	
	// is current time in the registration time in order to register for classes
	boolean isInRegistrationTime(Semester semester);
}
