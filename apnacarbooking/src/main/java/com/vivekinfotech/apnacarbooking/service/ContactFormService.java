package com.vivekinfotech.apnacarbooking.service;

import java.util.List;

import com.vivekinfotech.apnacarbooking.model.ContactForm;

public interface ContactFormService {
     
	
	     public ContactForm saveContactFormService(ContactForm contactForm);
	     
	     public List<ContactForm> readAllContactFormService();
	     
	     public  void deleteContactServiceById(int id);
	     
	     
}
