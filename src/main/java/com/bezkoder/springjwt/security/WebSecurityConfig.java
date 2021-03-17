package com.bezkoder.springjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bezkoder.springjwt.security.jwt.AuthEntryPointJwt;
import com.bezkoder.springjwt.security.jwt.AuthTokenFilter;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//		 securedEnabled = true,
//		 jsr250Enabled = true,
//		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private AuthTokenFilter authTokenFilter;

	// we can use either way . @Autuwired or creating an instance by the method specified below
//	@Bean
//	public AuthTokenFilter authenticationJwtTokenFilter() {
//		return new AuthTokenFilter();
//	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			// JSESSIONID Sẽ ko dc khởi tạo và ko bị sữ dụng để authenthicate subsequent request. Tất cả dửa vào authorization header đã dc tự động bỏ vào khi request
			// We are using JWT which can be authenticated by itself. Hence, we dont need JSESSIONID 
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/api/auth/**",  "/registrationConfirm*" , "/getCurrentUser").permitAll()
			.antMatchers("/api/test/**", "/login").permitAll()
			.antMatchers("/api/course/**").hasAnyAuthority("ROLE_USER", "ROLE_TEACHER")
			.antMatchers("/api/teacher/**").hasAnyAuthority("ROLE_TEACHER")
			.antMatchers("/api/book/**").hasAnyAuthority("ROLE_USER", "ROLE_TEACHER")
			.antMatchers("/api/student/**").hasAnyAuthority("ROLE_USER", "ROLE_TEACHER")
			.antMatchers("/api/student/upload").hasAnyAuthority("ROLE_TEACHER")
			.anyRequest().authenticated();
		
//		 http.logout().disable();
//	        http.formLogin().disable();
//	        http.httpBasic().disable();
//	        http.anonymous().disable();
		http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
//		http.logout()
//		.logoutUrl("/api/auth/doLogout")
//		.logoutSuccessUrl("/api/course/test1")
//		.invalidateHttpSession(true)
//		.deleteCookies("JSESSIONID");
		
	}
	
}