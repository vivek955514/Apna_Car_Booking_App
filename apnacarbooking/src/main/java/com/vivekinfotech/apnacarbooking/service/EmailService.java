package com.vivekinfotech.apnacarbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
         
	@Autowired
    private JavaMailSender mailSender;
	
	@Async // Runs this method in a separate thread automatically
    public void sendSuccessEmail(String userEmail, String name, String carName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail); // Only sending to the booking user
            message.setSubject("Booking Confirmed: " + carName);
            message.setText("Hello " + name + ",\n\n" +
                            "Your booking for " + carName + " has been successfully processed!\n" +
                            "Our team will contact you shortly for pickup details.\n\n" +
                            "Safe travels,\nApna Car Booking Team");

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + userEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
	
	
	@Async
	public void sendCancellationEmail(String userEmail, String name) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(userEmail);
	    message.setSubject("Booking Cancelled - Apna Car Booking");
	    message.setText("Dear " + name + ",\n\nYour car booking has been cancelled by the administrator. " +
	                    "The vehicle is now back in our available inventory.\n\n" +
	                    "If you have questions, please contact support.\n\n" +
	                    "Safe travels,\nApna Car Booking Team");
	    mailSender.send(message);
	}
	
	@Async
	public void sendResetEmail(String userEmail, String resetLink) {
	    try {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(userEmail);
	        message.setSubject("Password Reset Request - Apna Car Booking");
	        
	        String content = "Hello,\n\n" +
	                        "You requested to reset your password. Please click the link below to set a new password:\n" +
	                        resetLink + "\n\n" +
	                        "This link will expire in 15 minutes.\n" +
	                        "If you did not request this, please ignore this email.\n\n" +
	                        "Best regards,\nApna Car Booking Team";
	        
	        message.setText(content);
	        mailSender.send(message);
	        
	        System.out.println("Password reset email sent successfully to: " + userEmail);
	    } catch (Exception e) {
	        System.err.println("Error sending reset email: " + e.getMessage());
	    }
	}
	
	
	
	@Async
	public void sendRegistrationSuccessEmail(String userEmail, String username) {
	    try {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(userEmail);
	        message.setSubject("Welcome to Apna Car Booking!");
	        
	        String content = "Hello " + username + ",\n\n" +
	                        "Thank you for registering with Apna Car Booking! " +
	                        "Your account has been created successfully.\n\n" +
	                        "You can now log in to book your favorite cars at the best prices.\n\n" +
	                        "Happy Journey,\nApna Car Booking Team";
	        
	        message.setText(content);
	        mailSender.send(message);
	        System.out.println("Registration success email sent to: " + userEmail);
	    } catch (Exception e) {
	        System.err.println("Error sending registration email: " + e.getMessage());
	    }
	}
	
}
