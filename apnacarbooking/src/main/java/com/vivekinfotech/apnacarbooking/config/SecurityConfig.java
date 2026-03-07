package com.vivekinfotech.apnacarbooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private CustomLogoutHandler custuHandler;

	@Autowired
	public void setCustuHandler(CustomLogoutHandler custuHandler) {
		this.custuHandler = custuHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		
		http
	    .csrf(csrf -> csrf.disable())
	    .authorizeHttpRequests(auth -> auth

	        // 1. Admin Dashboard: Strictly for ADMIN role
	        .requestMatchers("/admin/**").hasRole("ADMIN")

	        // 2. Booking and Contacts: Only for logged-in users (User OR Admin)
	        .requestMatchers("/book-car/**", "/booking-form/**", "/contacts", "/contactform")
	        .authenticated()

	        // 3. Public Pages: Everyone can access these (Home, About, Cars, Services, Login, Register, etc.)
	        .requestMatchers(
	                "/forgot-password", "/reset-password", "/", "/home", "/welcome", "/index",
	                "/about", "/cars", "/services", "/carDetails/**",
	                "/login", "/register"
	        ).permitAll()

	        // 4. Static Resources
	        .requestMatchers("/css/**", "/js/**", "/images/**", "/mycar/**", "/customcss/**")
	        .permitAll()

	        .anyRequest().authenticated()
	    )

	    .formLogin(form -> form
	        .loginPage("/login")
	        .loginProcessingUrl("/login")
	        .successHandler((request, response, authentication) -> {
	            var authorities = authentication.getAuthorities();
	            for (var auth : authorities) {
	                if (auth.getAuthority().equals("ROLE_ADMIN")) {
	                    response.sendRedirect("/admin/dashboard");
	                    return;
	                }
	            }
	            response.sendRedirect("/home");
	        })
	        .permitAll()
	    )

	    .logout(logout -> logout
	        .addLogoutHandler(custuHandler)
	        .logoutUrl("/dologout")
	        .logoutSuccessUrl("/login?logout=true")
	    )

	    // Access denied handling
	    .exceptionHandling(exception ->
	        exception.accessDeniedPage("/access-denied")
	    );
		return http.build();
	}
}
