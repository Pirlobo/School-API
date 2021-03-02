package com.bezkoder.springjwt.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.VerificationToken;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByAccessToken(String token);
    
    
    
    		@Query(value = "SET SQL_SAFE_UPDATES=0;\r\n" + 
    				"    UPDATE users\r\n" + 
    				"    SET is_active = true", nativeQuery = true)
    void enableUser();

}
