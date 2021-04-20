package com.bezkoder.springjwt.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bezkoder.springjwt.models.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {
	@Query(value = "select * from school.assignment as a where a.course_reg_id = ?1", nativeQuery = true)
	List<Assignment> getAllFilesByCourseId (Integer regId);
	
}
