package com.bezkoder.springjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.bezkoder.springjwt.security.jwt.AuthEntryPointJwt;
import com.bezkoder.springjwt.security.jwt.AuthTokenFilter;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
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
			// We are using JWT which can be authenticated by itself. Hence, we dont need JSESSIONID, so we set stateless
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/api/auth/**", "/registrationConfirm*", "/api/assignments/**", "/api/course/getCourseDescriptionByRegId/**", "/api/files/**", "/getCurrentUser").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/api/upload").hasAuthority("ROLE_TEACHER")
			.antMatchers("/api/uploadAssignment").hasAuthority("ROLE_TEACHER")
			.antMatchers("/api/course/**").hasAuthority("ROLE_USER")
			.antMatchers("/api/teacher/**").hasAuthority("ROLE_TEACHER")
			.antMatchers("/api/profile/editProfile").hasAnyAuthority("ROLE_USER")
			.antMatchers("/api/book/**").hasAnyAuthority("ROLE_USER", "ROLE_TEACHER")
			.antMatchers("/api/student/**").hasAnyAuthority("ROLE_USER", "ROLE_TEACHER")
			.antMatchers("/api/auth/sendEmail").hasAnyAuthority("ROLE_TEACHER", "ROLE_USER")
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