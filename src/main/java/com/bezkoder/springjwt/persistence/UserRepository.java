 package com.bezkoder.springjwt.persistence;



 import java.util.List;
 import java.util.Optional;


 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.stereotype.Repository;

 import com.bezkoder.springjwt.models.User;

 @Repository
 public interface UserRepository extends JpaRepository<User, Long> {
	 
 	Optional<User> findByUsername(String userName);

 	Optional<User> findByEmail(String email);
 	
 	@Query(value = "select * from password_reset_token as t where t.token = ?1", nativeQuery = true)
 	List<User> test();
 	
 	Boolean existsByUsername(String username);

 	Boolean existsByEmail(String email);
 	

 	
 	
 	
 }
