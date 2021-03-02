package com.bezkoder.springjwt.security.service;
import java.sql.Date;

import org.springframework.stereotype.Service;

@Service
public interface ICalendarService {

	// get Current Time
	public Date getCurrentTime() ;
		
	// prior time to register for classes. In this case, default time is applied to all students
	public Date getDefaultTime() ;
	
	
	// due day to return rented books
	public Date getDefaultDueDate() ;
	
	// is current time before the registry time zone to register for classes
	boolean isBeforeDefaultTime();
}

