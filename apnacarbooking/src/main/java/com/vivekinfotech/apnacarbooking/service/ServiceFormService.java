package com.vivekinfotech.apnacarbooking.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vivekinfotech.apnacarbooking.model.ServiceAddForm;

public interface ServiceFormService {
   
	         public ServiceAddForm    addService(ServiceAddForm serviceAddForm,MultipartFile multipartFile) throws Exception;
	         
	         public List<ServiceAddForm> readAllServices();
	         
	         public boolean deleteService(int id)throws Exception ;
	         
	         public ServiceAddForm getById(int id);
	         
	         public boolean updateService(ServiceAddForm serviceAddForm, MultipartFile multipartFile) throws Exception ;
}
