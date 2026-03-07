package com.vivekinfotech.apnacarbooking.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class RegistrationDto {
    
	
	@NotEmpty(message = "Username is required")
    private String username;
     
	@NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String useremail;
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String confirmPassword; // Only for UI validation
}
