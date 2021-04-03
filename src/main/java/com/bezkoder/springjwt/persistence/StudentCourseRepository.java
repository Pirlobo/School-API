package com.bezkoder.springjwt.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId>{

	@Query(value = "select * from school.student_course as sc where sc.user_id = ?1 and course_id = ?2", nativeQuery = true)
	StudentCourse findStudentCourseById(Long userId, Integer courseId);
	
	@Query(value = "SELECT * FROM student_course as sc where sc.waitlisted_rank > ?1", nativeQuery = true)
	List<StudentCourse> findAfterRank(Integer rank);
}
