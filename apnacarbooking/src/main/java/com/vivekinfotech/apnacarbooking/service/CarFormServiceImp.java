package com.vivekinfotech.apnacarbooking.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vivekinfotech.apnacarbooking.dao.CarAddFormCurd;
import com.vivekinfotech.apnacarbooking.model.CarAddForm;
import com.vivekinfotech.apnacarbooking.model.ServiceAddForm;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@Service
public class CarFormServiceImp implements CarFormService {

	private CarAddFormCurd carAddFormCurd;
	 @Autowired
	public void setCarAddFormCurd(CarAddFormCurd carAddFormCurd) {
		this.carAddFormCurd = carAddFormCurd;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public CarAddForm addCar(CarAddForm carAddForm, MultipartFile multipartFile) throws Exception {

		CarAddForm save = null;
		try {

			save = carAddFormCurd.save(carAddForm);
			

			if (save != null) {

				String path = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\mycar\\"
						+ multipartFile.getOriginalFilename();
				byte[] bytes = multipartFile.getBytes();
				FileOutputStream fos = new FileOutputStream(path);
				fos.write(bytes);
				
		
			}

		} catch (Exception e) {
			
			save = null;
			throw e;
		}
		return save;

	}

	@Override
	public List<CarAddForm> readAllCars() {
		
		return carAddFormCurd.findAll() ;
	}

	@Override
	public Optional<CarAddForm> readCar(int id) {
		// TODO Auto-generated method stub
		return  carAddFormCurd.findById(id) ;
	}

	@Override
	public void updateCarStatus(int carId, String status) {
		
		Optional<CarAddForm> carOpt = carAddFormCurd.findById(carId);
	    if (carOpt.isPresent()) {
	        CarAddForm car = carOpt.get();
	        car.setStatus(status); // Sets status to "Booked"
	        carAddFormCurd.save(car);
	    }
	}
     
	@Transactional(rollbackOn = Exception.class)
	@Override
	public boolean updateCar(CarAddForm carAddForm, MultipartFile multipartFile) throws Exception {
		
		boolean updated  = false;
		try {
			
			 CarAddForm existing = carAddFormCurd.findById(carAddForm.getId()).orElse(null);
			 if(existing==null) {
				 return false;
			 }
			// Step 2 — Update fields
			 existing.setId(carAddForm.getId());
			 existing.setCategory(carAddForm.getCategory());
			 existing.setCarName(carAddForm.getCarName());
			 existing.setBrand(carAddForm.getBrand());
			 existing.setModel(carAddForm.getModel());
			 existing.setPrice(carAddForm.getPrice());
			 existing.setDescription(carAddForm.getDescription());
			 existing.setStatus(carAddForm.getStatus());
			 
			// Step 3 — If new image uploaded
			 
			 if(!multipartFile.isEmpty()) {
				 
				// Delete old image
					String oldImagePath = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\mycar\\"
							+ existing.getCarImage();

					File oldFile = new File(oldImagePath);

					if (oldFile.exists()) {

						oldFile.delete();

				  }
					
					// Save new image

					@Nullable
					String neworiginalFilename = multipartFile.getOriginalFilename();

					existing.setCarImage(neworiginalFilename);

					String path = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\mycar\\"
							+ neworiginalFilename;

					byte[] bytes = multipartFile.getBytes();
					FileOutputStream fos = new FileOutputStream(path);
					fos.write(bytes);
					fos.close();
					
					
					
			 } 
				// Step 4 — Save updated record
			 carAddFormCurd.save(existing);

				updated = true;
			 
		}catch(Exception e) {
			updated = false;
			throw e;
			
		}
		
		  
		return updated;
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deleteCar(int id) throws Exception{
		boolean deleted = false;

		try {

			 CarAddForm car = carAddFormCurd.findById(id).orElse(null);
			if (car == null) {
				return false;
			}

			// Image file path
			String imagePath = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\myservice\\"
					+ car.getCarImage();

			// Delete image from folder

			File file = new File(imagePath);
			if (file.exists()) {
				file.delete();
			}

			// Delete record from database
			carAddFormCurd.deleteById(id);

			deleted = true;

		} catch (Exception e) {

			deleted = false;
			throw e; // rollback
		}

		return deleted;
	}

	
}
