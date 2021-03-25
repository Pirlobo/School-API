package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId>{

	@Query(value = "select * from student_course as sc where sc.user_id = ?1 and sc.course_id = ?2", nativeQuery = true)
	StudentCourse findStudentCourseById(Long userId, Integer courseId);
}
