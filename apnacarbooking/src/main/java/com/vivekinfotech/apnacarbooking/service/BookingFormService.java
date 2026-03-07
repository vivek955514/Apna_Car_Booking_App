package com.vivekinfotech.apnacarbooking.service;

import java.util.List;

import com.vivekinfotech.apnacarbooking.model.BookingCarForm;
import com.vivekinfotech.apnacarbooking.model.BookingForm;
   
public interface BookingFormService {
     
	    public BookingForm  saveBookingFormService(BookingForm bookingForm);
	    
	    public List<BookingCarForm> readAllBookingService();
	     
	     public  void deleteBookingServiceById(int id);

		public  BookingCarForm saveBookingCarFormService(BookingCarForm bookingCarForm);
		
		public void cancelCarBooking(int bookingId);
	     
	     
}
