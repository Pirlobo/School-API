package com.bezkoder.springjwt.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface CoursePaginationRepository extends PagingAndSortingRepository<Course, Integer>{
	public static final String query2 = "select * from course as c inner join subject as s on s.id = c.subject_id inner join term as t on t.year = ?1 where s.subject_code like ?2% ORDER BY ?3";
	
	@Query(value = "select * from course as c inner join  subject as s on s.id = c.subject_id inner join term as t on t.year = ?1 where s.subject_code like ?2%", nativeQuery=true)
	Page<Course> findAllByTitle(Integer year, String title, 
			Pageable pageable);
}
