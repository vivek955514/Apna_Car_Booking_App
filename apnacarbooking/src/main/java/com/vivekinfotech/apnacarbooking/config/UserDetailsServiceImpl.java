package com.vivekinfotech.apnacarbooking.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vivekinfotech.apnacarbooking.dao.UserAccountDao;

import com.vivekinfotech.apnacarbooking.model.UserAccount;

import jakarta.annotation.PostConstruct;
@Service
public class UserDetailsServiceImpl implements  UserDetailsService {
        
	private UserAccountDao userAccountDao;
	
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = userAccountDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(account.getUsername())
                   .password(account.getPassword())
                   .roles(account.getRole()) // Spring adds ROLE_ prefix automatically
                   .build();
    }

    @PostConstruct
    public void init() {
        // Create the Super Admin if the table is empty
        if (userAccountDao.count() == 0) {
            UserAccount admin = new UserAccount();
            admin.setUsername("vivek");
            admin.setUseremail("raiv7453@gmail.com");
            admin.setPassword(passwordEncoder.encode("vivek123"));
            admin.setRole("ADMIN");
            userAccountDao.save(admin);
        }
        
    }
	
	

}
