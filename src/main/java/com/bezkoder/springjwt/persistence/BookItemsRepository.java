package com.bezkoder.springjwt.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.book.BookItems;

@Repository
public interface BookItemsRepository extends JpaRepository<BookItems, Integer> {

    

}
