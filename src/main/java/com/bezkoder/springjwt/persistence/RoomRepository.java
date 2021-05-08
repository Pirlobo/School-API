package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Room;



@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    

}