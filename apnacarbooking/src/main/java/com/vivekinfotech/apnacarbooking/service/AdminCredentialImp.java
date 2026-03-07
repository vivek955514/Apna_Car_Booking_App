package com.vivekinfotech.apnacarbooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vivekinfotech.apnacarbooking.dao.UserAccountDao;

import com.vivekinfotech.apnacarbooking.model.UserAccount;


@Service
public class AdminCredentialImp implements AdminCredentialService {

	
private UserAccountDao  userAccountDao;


	@Autowired
	public void setUserAccountDao(UserAccountDao userAccountDao) {
	this.userAccountDao = userAccountDao;
}

	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	
	
	@Override
	public String checkAdminCredential(String oldusername, String oldpassword) {
              
		Optional<UserAccount> byUsername = userAccountDao.findByUsername(oldusername);
		if(byUsername.isPresent()) {
		 UserAccount userAccount = byUsername.get(); 
		
		boolean matches = passwordEncoder.matches(oldpassword, userAccount.getPassword());
		  
		if( matches) {
			return "SUCCESS";
		}else {
			
			return "WRONG OLD CREDENTIAL";
		}
		
	}else {
		
		return "WRONG OLD CREDENTIAL";
	}
	}

	@Override
	public String updateAdminCredential(String newusername, String newpassword, String oldusername) {
		
		
		int updateCredentials = userAccountDao.updateCredentials(newusername, passwordEncoder.encode(newpassword), oldusername);
		
		if(updateCredentials==1) {
			
			return " CREDENTIAL UPDATED SUCCESSFULLY  AND DO LOGIN";
		}
		else {
			
			return "FAILED TO UPDATE";
		}
		
	}



	@Override
	public UserAccount saveUser(UserAccount newUser) {
		
	 	UserAccount newusersave = userAccountDao.save(newUser);
	 	
	 	return  newusersave;
		
	}
     
	      	 
	      
}
