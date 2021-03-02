//package com.bezkoder.springjwt.security.services;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Set;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.bezkoder.springjwt.models.User;
//import com.bezkoder.springjwt.persistence.UserRepository;
//
//@Service
//@Transactional
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//	private static final String ROLE_USER = "ROLE_USER";
//
//	@Autowired
//	private UserRepository userRepository;
//
//	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//		final User user = userRepository.findByEmail(email).orElse(null);
//
//		if (user == null) {
//			throw new UsernameNotFoundException("No user found with username: " + email);
//		}
//
//		// return new
//		// org.springframework.security.core.userdetails.User(user.getEmail(),
//		// user.getPassword(), user.getEnabled(), true, true, true,
//		// getAuthorities(ROLE_USER));
//		return new UserDetailsImpl(user);
//	}
//
//}





package com.bezkoder.springjwt.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.persistence.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
	}

}
