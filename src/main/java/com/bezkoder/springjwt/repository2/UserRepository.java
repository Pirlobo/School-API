//package com.bezkoder.springjwt.repository2;
//
//import java.util.List;
//import java.util.Optional;
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.bezkoder.springjwt.models.Student;
//
//@Repository
//public interface UserRepository extends JpaRepository<Student, Long> {
//	Optional<Student> findByUsername(String username);
//
//	Optional<Student> findByEmail(String email);
//	
//	@Query(value = "select * from password_reset_token as t where t.token = ?1", nativeQuery = true)
//	List<Student> test();
//	
//	Boolean existsByUsername(String username);
//
//	Boolean existsByEmail(String email);
//	
//
//	
//	
//	
//}
