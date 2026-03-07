package com.vivekinfotech.apnacarbooking.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="bookingform")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingForm {
          
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	       private int id;
	  
	@NotEmpty(message="name cant be empty")
	@NotBlank(message="name cant be blank")
	@Size(min=2, max=30, message="name must contain only alphabet")
	@Pattern(regexp = "^[A-Za-z ]{2,50}$", message = "Name must contain only letters and spaces")
	       private String name;
	  
		@NotEmpty(message="source cant be empty")
		@NotBlank(message="source cant be blank")
		@Column(name = "from_location")
	       private String from;

	    @Email(message = "Email is not valid")
	    @NotBlank(message = "Email is required")
		 private String email;
	    
		@NotEmpty(message=" destination cant be empty")
		@NotBlank(message="destination  cant be blank")
		@Size(min=2, max=100, message=" invalid destination ")
		@Column(name = "to_location")
	       private String to;
		@NotNull(message = "Time is required")
	       private LocalTime time;
		 @NotNull(message = "Date is required")
	       private LocalDate date;
		 @NotBlank(message = "Comfort selection is required")
	       private String comfort;
	       
		 @Min(value=1,message="adult can be at least 1")
		 @Max(value=4 , message="adult can be at most 4")
	       private int adult;
	       
		 @Max(value=3 ,message="children can be at most 3")
	       private int children;
	       
		  
			@NotEmpty(message=" message cant be empty")
			@NotBlank(message="message  cant be blank")
			@Size(min=2, max=600, message=" invalid  message ")
	       private String  message;
	       
	       
	       
	         
}
