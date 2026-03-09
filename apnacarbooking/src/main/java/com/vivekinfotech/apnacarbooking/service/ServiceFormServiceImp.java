package com.vivekinfotech.apnacarbooking.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vivekinfotech.apnacarbooking.dao.ServiceFormCrud;
import com.vivekinfotech.apnacarbooking.model.ServiceAddForm;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@Service
public class ServiceFormServiceImp implements ServiceFormService {

	private ServiceFormCrud serviceFormCrud;

	@Autowired
	public void setServiceFormCrud(ServiceFormCrud serviceFormCrud) {
		this.serviceFormCrud = serviceFormCrud;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public ServiceAddForm addService(ServiceAddForm serviceAddForm, MultipartFile multipartFile) throws Exception {

	    ServiceAddForm save = null;

	    try {

	        save = serviceFormCrud.save(serviceAddForm);

	        if (save != null && !multipartFile.isEmpty()) {

	            // Dynamic upload directory
	            String uploadDir = System.getProperty("user.dir") + "/uploads/services/";

	            File dir = new File(uploadDir);
	            if (!dir.exists()) {
	                dir.mkdirs();
	            }

	            String fileName = multipartFile.getOriginalFilename();
	            String filePath = uploadDir + fileName;

	            FileOutputStream fos = new FileOutputStream(filePath);
	            fos.write(multipartFile.getBytes());
	            fos.close();

	            serviceAddForm.setImage(fileName);
	        }

	    } catch (Exception e) {
	        save = null;
	        throw e;
	    }

	    return save;
	}

	@Override
	public List<ServiceAddForm> readAllServices() {

		return serviceFormCrud.findAll();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean deleteService(int id) throws Exception {
		boolean deleted = false;

		try {

			ServiceAddForm service = serviceFormCrud.findById(id).orElse(null);
			if (service == null) {
				return false;
			}

			// Image file path
			String imagePath = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\myservice\\"
					+ service.getImage();

			// Delete image from folder

			File file = new File(imagePath);
			if (file.exists()) {
				file.delete();
			}

			// Delete record from database
			serviceFormCrud.deleteById(id);

			deleted = true;

		} catch (Exception e) {

			deleted = false;
			throw e; // rollback
		}

		return deleted;
	}

	@Override
	public ServiceAddForm getById(int id) {

		return serviceFormCrud.findById(id).orElse(null);
	}

	@Override
	public boolean updateService(ServiceAddForm serviceAddForm, MultipartFile multipartFile) throws Exception {

		boolean update = false;

		try {

			ServiceAddForm existing = serviceFormCrud.findById(serviceAddForm.getId()).orElse(null);

			if (existing == null) {

				return false;
			}

			// Step 2 — Update fields
			existing.setTitle(serviceAddForm.getTitle());
			existing.setDescription(serviceAddForm.getDescription());

			// Step 3 — If new image uploaded
			if (!multipartFile.isEmpty()) {
				// Delete old image

				String oldImagePath = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\myservice\\"
						+ existing.getImage();

				File oldFile = new File(oldImagePath);

				if (oldFile.exists()) {

					oldFile.delete();

				}

				// Save new image

				@Nullable
				String neworiginalFilename = multipartFile.getOriginalFilename();

				existing.setImage(neworiginalFilename);

				String path = "C:\\SpringPractice\\apnacarbooking\\src\\main\\resources\\static\\myservice\\"
						+ neworiginalFilename;

				byte[] bytes = multipartFile.getBytes();
				FileOutputStream fos = new FileOutputStream(path);
				fos.write(bytes);
				fos.close();
			}

			// Step 4 — Save updated record
			serviceFormCrud.save(existing);

			update = true;

		}

		catch (Exception e) {

			update = false;
			throw e;

		}
		return update;
	}

}
