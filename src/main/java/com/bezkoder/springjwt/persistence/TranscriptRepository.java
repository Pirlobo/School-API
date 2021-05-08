package com.bezkoder.springjwt.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bezkoder.springjwt.models.Transcript;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {

}
