package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Teacher;



@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    

}