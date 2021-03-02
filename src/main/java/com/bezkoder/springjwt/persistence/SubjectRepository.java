package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Subject;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    

}