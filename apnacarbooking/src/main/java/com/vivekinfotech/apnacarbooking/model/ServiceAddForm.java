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
@Table(name="service")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
public class ServiceAddForm {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	  private int id;
	  private String image;
	  private String title;
	  private String description;
	  
	
	  
}
