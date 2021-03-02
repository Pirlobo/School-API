 package com.bezkoder.springjwt.persistence;


import javax.persistence.LockModeType;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Term;

@Repository
public interface TermRepository extends JpaRepository<Term, Integer> {

    
	
}