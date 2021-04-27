package com.bezkoder.springjwt.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bezkoder.springjwt.models.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

	@Query(value = "select * from school.files as f where f.course_reg_id = ?1", nativeQuery = true)
	List<FileDB> getAllFilesByCourseId (Integer regId);
	
	
}


