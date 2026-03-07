package com.vivekinfotech.apnacarbooking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity
@Table(name= "contacctform")

public class ContactForm {
        
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int id;

	    @NotEmpty(message = "Name cannot be empty")
	    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	   @Column(length = 30)
	   private String name;
	    @NotEmpty(message = "Email cannot be empty")
	    @Email(message = "Invalid email address")
	    @Column(length = 50)
	   private String email;
	    
	    @NotNull(message = "Phone number cannot be empty")
	    // Accepts only digits, 10–15 length
	    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10–15 digits")
	   private String   phone;
           
	    @NotEmpty(message = "Message cannot be empty")
	    @Size(min = 5, max = 500, message = "Message must be between 5 and 500 characters")
	   @Column(length = 500) 
	   private String message;
}
