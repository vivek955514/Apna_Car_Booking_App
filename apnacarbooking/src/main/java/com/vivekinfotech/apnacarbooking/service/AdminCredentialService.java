package com.vivekinfotech.apnacarbooking.service;

import com.vivekinfotech.apnacarbooking.model.UserAccount;

public interface AdminCredentialService {
    
	  
	      public String checkAdminCredential(String oldusername,String oldpassword);
	      

	      public String updateAdminCredential( String  newusername ,String newpassword ,String oldusername);


		  public UserAccount saveUser(UserAccount newUser);
	      
	      
}
