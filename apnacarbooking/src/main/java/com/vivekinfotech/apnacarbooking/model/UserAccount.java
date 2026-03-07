package com.vivekinfotech.apnacarbooking.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int id;
	 @Column(unique = true, nullable = false)
	   private String username;
	 @Column(nullable = false)
	   private String password;
	 
	 @Column(unique = true, nullable = false)
	   private String useremail;
	   
	   /** * Stores the authority level: 
	     * "ADMIN" -> Access to /admin/** * "USER"  -> Access to /book-car/**
	     */
	    private String role;
	    
	 // --- Forgot Password Fields ---
	    
	    @Column(name = "reset_password_token")
	    private String resetPasswordToken;

	    @Column(name = "token_expiry")
	    private LocalDateTime tokenExpiry;
	   
}
