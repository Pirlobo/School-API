package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bezkoder.springjwt.models.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query ( value = "select * from teacher where user_id = ?1 ", nativeQuery = true)
    public Teacher findTeacherByUser (Long userId);

}