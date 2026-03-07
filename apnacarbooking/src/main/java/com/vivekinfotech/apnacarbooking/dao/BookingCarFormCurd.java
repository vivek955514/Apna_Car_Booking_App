package com.vivekinfotech.apnacarbooking.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vivekinfotech.apnacarbooking.model.BookingCarForm;

@Repository
public interface BookingCarFormCurd  extends JpaRepository<BookingCarForm, Integer> {
           
	    
	  
	    	
}
