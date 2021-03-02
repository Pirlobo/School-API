package com.bezkoder.springjwt.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {

    

}
