package com.bezkoder.springjwt.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.book.Authors;

@Repository
public interface AuthorRepository extends JpaRepository<Authors, Integer> {

    

}
