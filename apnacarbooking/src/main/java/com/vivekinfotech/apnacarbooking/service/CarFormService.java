package com.vivekinfotech.apnacarbooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.vivekinfotech.apnacarbooking.model.CarAddForm;



public interface CarFormService {
                 
	   public CarAddForm    addCar(CarAddForm carAddForm ,MultipartFile multipartFile) throws Exception;
	   
	   public List<CarAddForm> readAllCars();
	   
	   public Optional<CarAddForm> readCar(int id);
	   
	   public void updateCarStatus(int carId, String status);

	   public boolean updateCar(CarAddForm carAddForm, MultipartFile multipartFile) throws Exception;

	   public boolean deleteCar(int id) throws Exception;
	   
}
