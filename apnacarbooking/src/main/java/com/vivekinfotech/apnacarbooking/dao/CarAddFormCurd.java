package com.vivekinfotech.apnacarbooking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vivekinfotech.apnacarbooking.model.CarAddForm;
@Repository
public interface CarAddFormCurd extends JpaRepository<CarAddForm, Integer> {
    
	       
}
