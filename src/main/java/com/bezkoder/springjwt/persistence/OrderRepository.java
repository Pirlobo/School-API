package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.book.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

    

}
