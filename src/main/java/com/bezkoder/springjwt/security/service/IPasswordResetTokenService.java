package com.bezkoder.springjwt.security.service;
import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.User;
public interface IPasswordResetTokenService {

	public PasswordResetToken findByToken(String token);
	
	public PasswordResetToken findByUser(User user);
	
	public PasswordResetToken findByCode(String code);
	
	public void deleteAll();
	
}
