//package com.bezkoder.springjwt.security.services;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.bezkoder.springjwt.models.Role;
//import com.bezkoder.springjwt.models.User;
//import com.bezkoder.springjwt.persistence.UserRepository;
//
//
//public class UserDetailsImpl implements UserDetails{
//	@Autowired
//	UserRepository userRepository;
//	
//	private User user;
//    
//    public UserDetailsImpl(User user) {
//        this.user = user;
//    }
//    
//	@Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//		
//		
//		
//        Set<Role> roles = user.getRoles();
//        
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        
//        for (Role role : roles) {
//        	
//            authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
//        }
//         
//        return authorities;
//    }
//
//	public String getEmail() {
//		return user.getEmail();
//	}
//	
//	@Override
//    public String getPassword() {
//        return user.getPassword();
//    }
// 
//    @Override
//    public String getUsername() {
//        return user.getEmail();
//    }
// 
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
// 
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
// 
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
// 
//    @Override
//    public boolean isEnabled() {
//        if (user.isActive() == true) {
//			return true;
//		} else {
//			return false;
//		}
//    }
//	public Optional<User> getUser() {
//			User user = userRepository.findByEmail("bonguyens2001@gmail.com").orElse(null);
//		    Optional<User> opt = Optional.ofNullable(user);
//		    return opt;
//	
//	}
// 
//}



package com.bezkoder.springjwt.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bezkoder.springjwt.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;
	
	private boolean isActive;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public UserDetailsImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities, boolean isActive) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.isActive = isActive;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword(), 
				authorities,
				user.isActive());
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isActive(), true, true, true, authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
