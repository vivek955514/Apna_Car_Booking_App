package com.vivekinfotech.apnacarbooking.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vivekinfotech.apnacarbooking.dao.UserAccountDao;
import com.vivekinfotech.apnacarbooking.model.RegistrationDto;
import com.vivekinfotech.apnacarbooking.model.UserAccount;
import com.vivekinfotech.apnacarbooking.service.AdminCredentialService;
import com.vivekinfotech.apnacarbooking.service.EmailService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    
	@Autowired
    private UserAccountDao userAccountDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
   private EmailService   emailService;
   
   
    @Autowired
    public void setEmailService(EmailService emailService) {
	this.emailService = emailService;
}

	private AdminCredentialService  adminCredentialService;
    
    @Autowired
    public void setAdminCredentialService(AdminCredentialService adminCredentialService) {
		this.adminCredentialService = adminCredentialService;
	}



    
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                HttpServletRequest req, Model model) {
        
        // 1. Handle Logout message from your CustomLogoutHandler
        ServletContext servletContext = req.getServletContext();
        Object logoutAttr = servletContext.getAttribute("logout");
        if (logoutAttr instanceof Boolean && (Boolean) logoutAttr) {
            model.addAttribute("logout", true);
            servletContext.removeAttribute("logout");
        }

        // 2. The 'error' parameter is provided by Spring Security on failure
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }

        return "adminlogin"; // Your HTML file name
    }
    
    
	@GetMapping("/register")
    public String showRegisterPage(Model model) {
    	  
    	model.addAttribute("regDto", new RegistrationDto());
    	return "register";
    	    
    }
    
    
	
    
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("regDto") RegistrationDto regDto, 
                                     BindingResult result, 
                                     RedirectAttributes ra) {
        // 1. Check for validation errors (size, empty, etc.)
        if (result.hasErrors()) {
            return "register";
        }

        // 2. Business Logic: Check if passwords match
        if (!regDto.getPassword().equals(regDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.regDto", "Passwords do not match!");
            return "register";//html page name
        }

        // 3. Check if username is already taken
        if (userAccountDao.findByUsername(regDto.getUsername()).isPresent()) {
            result.rejectValue("username", "error.regDto", "Username already exists!");
            return "register";
        }

        // 4. Map DTO to Entity and Save
        UserAccount newUser = new UserAccount();
        newUser.setUsername(regDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(regDto.getPassword()));
        newUser.setUseremail(regDto.getUseremail());
        newUser.setRole("USER"); // Standard registration is always USER
           
        UserAccount saveUser = adminCredentialService.saveUser(newUser);
        
        emailService.sendRegistrationSuccessEmail(saveUser.getUseremail(), saveUser.getUsername());
     // Updated the message to mention the email
        ra.addFlashAttribute("message", "Registration successful! A confirmation email has been sent to " + regDto.getUseremail()+ ". You can now login.");
        return "redirect:/login";
    }
    
    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied"; 
    }
    
    
    // Show Forgot Password Page
    
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot-password";
    }
    
    
 // 2. Handle the email submission
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("username") String username,
    		RedirectAttributes ra ,HttpServletRequest request ) {
        Optional<UserAccount> userOpt = userAccountDao.findByUsername(username);
        
        if (userOpt.isPresent()) {
            UserAccount user = userOpt.get();
            String token = UUID.randomUUID().toString(); // Generate unique token
            
            user.setResetPasswordToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(15)); // Valid for 15 mins
            userAccountDao.save(user);
            
            //  change by mail
         // DYNAMIC LINK: Detects http/https and the correct port (8081) automatically
         // FIX: Use ServletUriComponentsBuilder to detect the actual public URL
            // It looks at "X-Forwarded" headers sent by ngrok
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(null)
                    .build()
                    .toUriString();
            
            System.out.println("base url is %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+baseUrl+"***************S");
            String resetLink = baseUrl + "/reset-password?token=" + token;
            
            System.out.println("reset links is #$$$$$$$$$$%$%$"+resetLink);
            
            emailService.sendResetEmail(user.getUseremail(), resetLink);
            
            ra.addFlashAttribute("message", "Reset link sent to your registered username/email.");
        } else {
            ra.addFlashAttribute("error", "Username not found.");
        }
        return "redirect:/forgot-password";
    }

    // 3. Show Reset Form (from Email Link)
    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    // 4. Update the actual password
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token, 
                                     @RequestParam("password") String newPassword, 
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     RedirectAttributes ra) {
        
        // 1. Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error", "Passwords do not match!");
            return "redirect:/reset-password?token=" + token;
        }

        // 2. Find user by token
        Optional<UserAccount> userOpt = userAccountDao.findByResetPasswordToken(token);
        
        if (userOpt.isPresent()) {
            UserAccount user = userOpt.get();
            
            // 3. Check if token has expired
            if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
                ra.addFlashAttribute("error", "The reset link has expired. Please request a new one.");
                return "redirect:/forgot-password";
            }

            // 4. Success: Update password and CLEAR token fields
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            user.setTokenExpiry(null);
            userAccountDao.save(user);

            ra.addFlashAttribute("message", "Your password has been reset successfully. Please login.");
            return "redirect:/login";
        }

        ra.addFlashAttribute("error", "Invalid password reset token.");
        return "redirect:/forgot-password";
    }
    
}
    

