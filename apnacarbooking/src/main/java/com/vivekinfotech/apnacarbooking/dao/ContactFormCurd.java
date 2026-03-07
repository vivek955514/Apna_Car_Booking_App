package com.vivekinfotech.apnacarbooking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vivekinfotech.apnacarbooking.model.ContactForm;

@Repository
public interface ContactFormCurd  extends JpaRepository<ContactForm, Integer>{
	
	
	            @Override
	            public  <S extends ContactForm> S save(S entity);
	            	
	            @Override
	            public  List<ContactForm> findAll() ;
	            
	            @Override
	            public  void deleteById(Integer id);
	           
	            		
	           
	           	
	           

}
