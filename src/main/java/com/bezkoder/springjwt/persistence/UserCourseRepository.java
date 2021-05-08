package com.bezkoder.springjwt.persistence;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bezkoder.springjwt.models.Course;
import com.bezkoder.springjwt.models.Grade;
import com.bezkoder.springjwt.models.IsPassed;
import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;
import com.bezkoder.springjwt.models.User;

@Repository
public interface UserCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {

	@Transactional
	@Modifying
	@Query("update StudentCourse sc set sc.grade = :letter, sc.percentage = :percentage where sc.user = :userId and sc.course = :courseRegId")
	void update(@Param(value = "letter") Grade letter, @Param(value = "percentage") Double percentage, @Param(value = "userId") User userId, @Param(value = "courseRegId") Course coureRegId);

	@Transactional
	@Modifying
	@Query("update StudentCourse sc set sc.grade = :letter, sc.percentage = :percentage, sc.finalGrade = :finalGrade where sc.user = :userId and sc.course = :courseRegId")
	void lastUpdate(@Param(value = "letter") Grade letter, @Param(value = "percentage") Double percentage, @Param(value = "finalGrade") Grade finalGrade, @Param(value = "userId") User userId, @Param(value = "courseRegId") Course coureRegId);

	@Transactional
	@Modifying
	@Query("update StudentCourse sc set sc.isPassed = :isPassed where sc.user = :userId and sc.course = :courseRegId")
	void setIsPassedStatus(@Param(value = "isPassed") IsPassed isPassed, @Param(value = "userId") User userId, @Param(value = "courseRegId") Course coureRegId);
}
