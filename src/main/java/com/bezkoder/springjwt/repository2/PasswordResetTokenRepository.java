//package com.bezkoder.springjwt.repository2;
//
//
//
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.bezkoder.springjwt.models.PasswordResetToken;
//import com.bezkoder.springjwt.models.Student;
//
//@Repository
//public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
//	@Query(value = "select * from password_reset_token as t where t.token = ?1", nativeQuery = true)
//    PasswordResetToken findByToken(String token);
//	
//	PasswordResetToken findByCode(String code);
//
//    PasswordResetToken findByUser(Student user);
//
//
//}
//
