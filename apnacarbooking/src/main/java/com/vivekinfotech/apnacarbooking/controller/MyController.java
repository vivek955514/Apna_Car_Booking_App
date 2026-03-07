package com.vivekinfotech.apnacarbooking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vivekinfotech.apnacarbooking.model.BookingCarForm;
import com.vivekinfotech.apnacarbooking.model.BookingForm;
import com.vivekinfotech.apnacarbooking.model.CarAddForm;
import com.vivekinfotech.apnacarbooking.model.ContactForm;
import com.vivekinfotech.apnacarbooking.model.ServiceAddForm;
import com.vivekinfotech.apnacarbooking.service.BookingFormService;
import com.vivekinfotech.apnacarbooking.service.CarFormService;
import com.vivekinfotech.apnacarbooking.service.ContactFormService;
import com.vivekinfotech.apnacarbooking.service.EmailService;
import com.vivekinfotech.apnacarbooking.service.ServiceFormService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class MyController {

	private ContactFormService contactFormService;
	private BookingFormService bookingFormService;

	private ServiceFormService serviceFormService;

	private CarFormService carFormService;
	
	private EmailService  emalEmailService;
	
    @Autowired
	public void setEmalEmailService(EmailService emalEmailService) {
		this.emalEmailService = emalEmailService;
	}

	@Autowired
	public void setCarFormService(CarFormService carFormService) {
		this.carFormService = carFormService;
	}

	@Autowired
	public void setServiceFormService(ServiceFormService serviceFormService) {
		this.serviceFormService = serviceFormService;
	}

	@Autowired
	public void setBookingFormService(BookingFormService bookingFormService) {
		this.bookingFormService = bookingFormService;
	}

	@Autowired
	public void setContactFormService(ContactFormService contactFormService) {
		this.contactFormService = contactFormService;
	}

	@GetMapping(path = { "", "home", "welcome", "index" })
	public String welcomeView(HttpServletRequest req, Model model) {

		String requestURI = req.getRequestURI();
		System.out.println(requestURI);
		model.addAttribute("mycurrentpage", requestURI);
		model.addAttribute("bookingForm", new BookingForm());

		return "index";
	}

	

	@GetMapping("about")
	public String aboutView(HttpServletRequest req, Model model) {

		String requestURI = req.getRequestURI();
		model.addAttribute("mycurrentpage", requestURI);

		return "about";
	}

	@GetMapping("cars")
	public String carView(HttpServletRequest req, Model model) {

		String requestURI = req.getRequestURI();
		model.addAttribute("mycurrentpage", requestURI);

		// DATA COLLECTION
		List<CarAddForm> allCars = carFormService.readAllCars();

		model.addAttribute("allcars", allCars);

		return "cars";
	}

	@GetMapping("services")
	public String servicesView(HttpServletRequest req, Model model) {

		String requestURI = req.getRequestURI();
		model.addAttribute("mycurrentpage", requestURI);

		// DATA COLLECTION

		List<ServiceAddForm> allServices = serviceFormService.readAllServices();
		model.addAttribute("allservices", allServices);

		return "services";
	}

	@GetMapping("carDetails/{id}")
	public String carDetails(@PathVariable int id, Model model) {

		Optional<CarAddForm> car = carFormService.readCar(id);
		CarAddForm carAddForm = car.get();
		model.addAttribute("car", carAddForm);
		return "car-details";
	}

	@GetMapping("/book-car/{id}")
	public String bookCarPage(@PathVariable int id, Model model) {
		Optional<CarAddForm> car = carFormService.readCar(id);
		CarAddForm carAddForm = car.get();
		// Create the form object and PRE-SET the carId
	    BookingCarForm bookingCarForm = new BookingCarForm();
	    bookingCarForm.setCarId(id); // <--- THIS IS CRITICAL
	    
	    model.addAttribute("car", carAddForm);
	    model.addAttribute("bookingForm", bookingCarForm); // Match the th:object name
	    return "booking-page";
	}

	@GetMapping("contacts")
	public String contactView(HttpServletRequest req, Model model) {

		String requestURI = req.getRequestURI();
		model.addAttribute("mycurrentpage", requestURI);
		model.addAttribute("contactForm", new ContactForm());

		return "contacts";
	}

	@PostMapping("contactform")
	public String contactForm(@Valid @ModelAttribute ContactForm conForm, BindingResult bindResult, Model model,
			RedirectAttributes rediAttributes) {

		if (bindResult.hasErrors()) {

			model.addAttribute("bindresult", bindResult);
			return "contacts";
		}

		ContactForm saveContactFormService = contactFormService.saveContactFormService(conForm);

		if (saveContactFormService != null) {

			rediAttributes.addFlashAttribute("message", "Message sent  Successfully");

		} else {

			rediAttributes.addFlashAttribute("message", "Something went wrong");
		}
		System.out.println(conForm);

		return "redirect:/contacts";
	}

	@PostMapping("bookingform")
	public String bookingForm(@Valid @ModelAttribute BookingForm bookingForm, BindingResult bindResult, Model model,
			RedirectAttributes rediAttributes) {

		if (bindResult.hasErrors()) {

			model.addAttribute("bindresult", bindResult);
			return "index";
		}

		else if (bookingForm.getAdult() + bookingForm.getChildren() > 4) {

			model.addAttribute("message", "The total no of adult and children cant exceed 4");
			return "index";

		}

		// Service
		BookingForm saveBookingFormService = bookingFormService.saveBookingFormService(bookingForm);
		if (saveBookingFormService != null) {

			rediAttributes.addFlashAttribute("message", "Booking  Successfully");

		} else {

			rediAttributes.addFlashAttribute("message", "Something went wrong");
		}

		return "redirect:/index";
	}
	
	
	
	@GetMapping("/booking-success")
	public String showConfirmation(Model model) {
	   
	    if (!model.containsAttribute("bookingDetails")) {
	        return "redirect:/cars";
	    }
	    return "booking-confirmation"; 
	}

	@PostMapping("booking-form")
	public String bookingCarForm(@Valid @ModelAttribute("bookingForm") BookingCarForm bookingForm, 
	                            BindingResult bindResult, 
	                            Model model,
	                            RedirectAttributes rediAttributes) {
	    
	    // 1. ALWAYS reload the car details first. 
	    // The template "booking-page" NEEDS the 'car' object to render the UI.
	    Optional<CarAddForm> carOpt = carFormService.readCar(bookingForm.getCarId());
	    
	    if (carOpt.isPresent()) {
	        model.addAttribute("car", carOpt.get());
	    } else {
	        // If the carId is missing or invalid, we can't show the booking page.
	        return "redirect:/cars";
	    }

	    // 2. Check for Validation Errors (name, email, etc.)
	    if (bindResult.hasErrors()) {
	      
	        return "booking-page"; 
	    }

	    // 3. Check for Business Logic Errors (Capacity)
	    if (bookingForm.getAdult() + bookingForm.getChildren() > 4) {
	        model.addAttribute("message", "The total no of adult and children cant exceed 4");
	        return "booking-page";
	    }

	    // 4. If all is good, Save
	    BookingCarForm savedForm = bookingFormService.saveBookingCarFormService(bookingForm);
	    
	    if (savedForm != null) {
	    	
	    	carFormService.updateCarStatus(bookingForm.getCarId(), "Booked");
	    	emalEmailService.sendSuccessEmail(savedForm.getEmail(), savedForm.getName(), carOpt.get().getCarName());
	    	
	    	// Use FlashAttributes so data survives the redirect
	        rediAttributes.addFlashAttribute("bookingDetails", savedForm);
	        rediAttributes.addFlashAttribute("carDetails", carOpt.get());
	        return "redirect:/booking-success";
	        
	    } else {
	    	rediAttributes.addFlashAttribute("message", "Database error occurred.");
	        return "redirect:/cars";
	    }

	  
	}
}
