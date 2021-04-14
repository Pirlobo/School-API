package com.bezkoder.springjwt.security.service;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.persistence.CalendarRepository;

@Service
public class CalendarService implements ICalendarService {

	@Autowired
	private CalendarRepository calendarRepository;

	public Date getCurrentTime() {
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		return date;

	}

	public Date getDefaultTime() {
		java.sql.Date date = calendarRepository.getOne(1).getDefaultDate();
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

	@Override
	public boolean isBeforeDefaultTime() {
		java.sql.Date currentTime = getCurrentTime();
		java.sql.Date defaultTime = getDefaultTime();
		if (currentTime.before(defaultTime)) {
			return true;
		}
		return false;
	}
}
