package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Building;


@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {

    

}

