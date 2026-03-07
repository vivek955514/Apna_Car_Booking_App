package com.vivekinfotech.apnacarbooking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vivekinfotech.apnacarbooking.model.ServiceAddForm;

public interface ServiceFormCrud  extends JpaRepository<ServiceAddForm, Integer>{

}
