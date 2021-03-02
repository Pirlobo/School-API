package com.bezkoder.springjwt.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.StudentCourse;
import com.bezkoder.springjwt.models.StudentCourseId;

@Repository
public interface UserCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {

    

}
