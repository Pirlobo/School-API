package com.bezkoder.springjwt.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService implements IPasswordResetTokenService{

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Override
	public PasswordResetToken findByToken(String token) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
		return passwordResetToken;
	}

	@Override
	public PasswordResetToken findByUser(User user) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user);
		return passwordResetToken;
	}

	@Override
	public PasswordResetToken findByCode(String code) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByCode(code);
		return passwordResetToken;
	}

	@Override
	public void deleteAll() {
		passwordResetTokenRepository.deleteAll();
	}

}
