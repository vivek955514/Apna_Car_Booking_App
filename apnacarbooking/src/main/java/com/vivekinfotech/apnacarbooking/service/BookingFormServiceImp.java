package com.vivekinfotech.apnacarbooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vivekinfotech.apnacarbooking.dao.BookingCarFormCurd;
import com.vivekinfotech.apnacarbooking.dao.BookingFormCurd;
import com.vivekinfotech.apnacarbooking.model.BookingCarForm;
import com.vivekinfotech.apnacarbooking.model.BookingForm;

import jakarta.transaction.Transactional;

@Service
public class BookingFormServiceImp implements BookingFormService {
   
	private  BookingFormCurd  bookingFormCurd;
	@Autowired
	public void setBookingFormCurd(BookingFormCurd bookingFormCurd) {
		this.bookingFormCurd = bookingFormCurd;
	}
	
	private BookingCarFormCurd   bookingCarFormCurd;
	
     @Autowired
	public void setBookingCarFormCurd(BookingCarFormCurd bookingCarFormCurd) {
		this.bookingCarFormCurd = bookingCarFormCurd;
	}

     private CarFormService carFormService;
     
     
   @Autowired
	public void setCarFormService(CarFormService carFormService) {
		this.carFormService = carFormService;
	}

  
   private EmailService  emailService;
   

    @Autowired
	public void setEmailService(EmailService emailService) {
	this.emailService = emailService;
}



	@Override
	public BookingCarForm saveBookingCarFormService(BookingCarForm bookingCarForm) {
		
		 return   bookingCarFormCurd.save( bookingCarForm);
		   
	}
	
	
	
	@Override
	public BookingForm saveBookingFormService(BookingForm bookingForm) {
		
		 return   bookingFormCurd.save(bookingForm);
		   
	}

	@Override
	public List<BookingCarForm> readAllBookingService() {
		
		return bookingCarFormCurd.findAll();
	}

	@Override
	public void deleteBookingServiceById(int id) {
		
		bookingFormCurd.deleteById(id);
		
	}


	@Transactional
	@Override
	public void cancelCarBooking(int bookingId) {
		Optional<BookingCarForm> bookingOpt = bookingCarFormCurd.findById(bookingId);
		
		if (bookingOpt.isPresent()) {
	        BookingCarForm booking = bookingOpt.get();
	        
	        // 1. Reset the Car to Available
	        carFormService.updateCarStatus(booking.getCarId(), "Available");
	        
	        // 2. Notify User
	        emailService.sendCancellationEmail(booking.getEmail(), booking.getName());
	        
	       
	        // Or delete if you prefer a clean table:
	        bookingCarFormCurd.deleteById(bookingId);
	    }
	}
		     
		
	}


