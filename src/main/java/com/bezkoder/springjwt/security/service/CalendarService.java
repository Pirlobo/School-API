package com.bezkoder.springjwt.security.service;

import java.sql.Date;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.models.Semester;
@Service
public class CalendarService implements ICalendarService {


	public Date getCurrentTime() {
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		return date;

	}

	@Override
	public Date getDefaultDueDate() {
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		int day = date.getDate();
		java.sql.Date dueDate = new Date(date.getYear(), date.getMonth(), day + 7);
		return dueDate;
	}

	// prior time to register for classes. In this case, default time is applied to
		// all students
	@Override
	public boolean isBeforeDefaultTime(Semester semester) {
		java.sql.Date currentTime = getCurrentTime();
		// 01 / 15 / 2022
		java.sql.Date registrationTime;
		if (semester == Semester.Fall) {
			registrationTime = new Date(122, 8, 15);
		} else {
			// January 15th 2022
//			registrationTime = new Date(121, 12, 15);
			
			// April 30 2021 
			registrationTime = new Date(121, 4, 30);
		}
		if (currentTime.before(registrationTime)) {
			return true;
		}
		return false;
	}
	@Override
	public boolean isAfterDefaultTime(Semester semester) {
		java.sql.Date currentTime = getCurrentTime();
		// 01 / 01 / 2022
		java.sql.Date registrationTime;
		if (semester == Semester.Fall) {
			registrationTime = new Date(122, 8, 1);
		} else {
			// January 1st 2022 
//			registrationTime = new Date(121, 12, 1);
			
			// January 1st 2021
			registrationTime = new Date(120, 12, 1);
		}
		if (currentTime.after(registrationTime)) {
			return true;
		}
		return false;
	
	}
	
	@Override
	public boolean isInRegistrationTime(Semester semester) {
		if (isBeforeDefaultTime(semester) && isAfterDefaultTime(semester)) {
			return true;
		}
		else {
			return false;
		}
	}
}
