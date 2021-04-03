package com.bezkoder.springjwt.persistence;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Course;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
	
	public static final String query = "select * from course as c inner join subject as s on s.id = c.subject_id  where s.subject_code like %?1%";
	
	public static final String query2 = "select * from course as c inner join subject as s on s.id = c.subject_id inner join term as t on t.year = ?1 where s.subject_code like ?2%";
	@Query(value = query2, nativeQuery=true)
    List<Course> findByTitle(Integer year, String title);
   
    @Modifying
	@Query(value =  "UPDATE school.course as c SET c.available = ?1 WHERE c.id = ?2" , nativeQuery = true)
    
    void setAvailable(Integer available, Integer id);
    
    @Modifying
   	@Query(value =  "UPDATE school.course as c SET c.waitlist = ?1 WHERE c.id = ?2", nativeQuery = true)
       void setWailist(Integer waitlist, Integer id); 
    
    @Modifying
   	@Query(value = "select * from course as c inner join subject as s on c.subject_id = s.id where s.subject_name= ?1", nativeQuery = true)
       List<Course> findByArea(String subjectName);		
    		
    		
}