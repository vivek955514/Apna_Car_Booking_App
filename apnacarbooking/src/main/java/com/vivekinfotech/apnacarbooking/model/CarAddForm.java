package com.vivekinfotech.apnacarbooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity

@Table(name="car")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Setter

public class CarAddForm {
   
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    private String carImage;     // stores image filename
	    private String category;
	    private String carName;
	    private String brand;
	    private String model;
	    private double price;        // price per day
	    private String description;
	    private String status;
	     
}
