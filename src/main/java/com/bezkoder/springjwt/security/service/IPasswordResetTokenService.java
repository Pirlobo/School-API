package com.bezkoder.springjwt.security.service;

import java.net.PasswordAuthentication;

import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.PasswordResetToken;
import com.bezkoder.springjwt.models.User;

import net.sf.ehcache.util.FindBugsSuppressWarnings;


public interface IPasswordResetTokenService {

	public PasswordResetToken findByToken(String token);
	
	public PasswordResetToken findByUser(User user);
	
	public PasswordResetToken findByCode(String code);
	
	public void deleteAll();
	
}
