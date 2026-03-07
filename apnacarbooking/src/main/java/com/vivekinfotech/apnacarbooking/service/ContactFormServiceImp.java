package com.vivekinfotech.apnacarbooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vivekinfotech.apnacarbooking.dao.ContactFormCurd;
import com.vivekinfotech.apnacarbooking.model.ContactForm;


@Service
public class ContactFormServiceImp  implements ContactFormService{
    
	 
	private ContactFormCurd contactFormCurd;
	
	@Autowired
	public void setContactFormCurd(ContactFormCurd contactFormCurd) {
		this.contactFormCurd = contactFormCurd;
	}



	@Override
	public ContactForm saveContactFormService(ContactForm contactForm) {
	       
	      return contactFormCurd.save(contactForm);
		   
		
	}



	@Override
	public List<ContactForm> readAllContactFormService() {
		
		return contactFormCurd.findAll() ;
	}



	@Override
	public void deleteContactServiceById(int id) {
		
		contactFormCurd.deleteById(id);
		
	}

}
