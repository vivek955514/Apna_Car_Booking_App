package com.vivekinfotech.apnacarbooking.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vivekinfotech.apnacarbooking.model.BookingCarForm;

import com.vivekinfotech.apnacarbooking.model.CarAddForm;
import com.vivekinfotech.apnacarbooking.model.ContactForm;
import com.vivekinfotech.apnacarbooking.model.ServiceAddForm;
import com.vivekinfotech.apnacarbooking.service.AdminCredentialService;
import com.vivekinfotech.apnacarbooking.service.BookingFormService;
import com.vivekinfotech.apnacarbooking.service.CarFormService;
import com.vivekinfotech.apnacarbooking.service.ContactFormService;
import com.vivekinfotech.apnacarbooking.service.ServiceFormService;

@Controller
@RequestMapping("admin")
public class AdminController {

	private ContactFormService contactFormService;
	private AdminCredentialService adminCredentialService;

	private BookingFormService bookingFormService;

	private ServiceFormService serviceFormService;

	private CarFormService carFormService;

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
	public void setAdminCredentialService(AdminCredentialService adminCredentialService) {
		this.adminCredentialService = adminCredentialService;
	}

	@Autowired
	public void setContactFormService(ContactFormService contactFormService) {
		this.contactFormService = contactFormService;
	}

	@GetMapping("dashboard")
	public String adminDashboard() {

		return "admin/admindashboard";
	}

	@GetMapping("readAllContacts")
	public String readAllContact(Model model) {

		List<ContactForm> allContactFormService = contactFormService.readAllContactFormService();

		model.addAttribute("allcontact", allContactFormService);
		return "admin/readallcontacts";
	}

	@GetMapping("readAllBookings")
	public String readAllBooking(Model model) {

		List<BookingCarForm> allBookingService = bookingFormService.readAllBookingService();

		model.addAttribute("allBooking", allBookingService);
		return "admin/readallbookings";
	}
    
	  
	
	
	@GetMapping("deleteContact/{id}")
	public String deleteContact(@PathVariable int id, RedirectAttributes redirectAttributes) {
		contactFormService.deleteContactServiceById(id);
		redirectAttributes.addFlashAttribute("message", "CONTACT DELETED SUCCESSFULLY");
		return "redirect:/admin/readAllContacts";

	}

	@GetMapping("deleteBooking/{id}")
	public String deleteBookingById(@PathVariable int id, RedirectAttributes redirectAttributes) {

		bookingFormService.deleteBookingServiceById(id);
		redirectAttributes.addFlashAttribute("message", "BOOKING  DELETED SUCCESSFULLY");
		return "redirect:/admin/readAllBookings";

	}

	@GetMapping("changeCredentials")
	public String changeCredentialView() {

		return "admin/changecredentials";
	}

	@GetMapping("addService")
	public String addserviceView() {

		return "admin/addservice";
	}

	@InitBinder
	public void stopBinding(WebDataBinder webDataBinder) {

		webDataBinder.setDisallowedFields("image");
	}

	@PostMapping("addService")
	public String addservice(@ModelAttribute ServiceAddForm serviceAddForm,
			@RequestParam("image") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {

		String originalFilename = multipartFile.getOriginalFilename();
		serviceAddForm.setImage(originalFilename);
		try {
			ServiceAddForm service = serviceFormService.addService(serviceAddForm, multipartFile);

			if (service != null) {

				redirectAttributes.addFlashAttribute("msg", "Service Added Successfully");
			} else {

				redirectAttributes.addFlashAttribute("msg", "SomeThing Went Wrong");
			}
		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("msg", "SomeThing Went Wrong");
		}
		return "redirect:/admin/addService";
	}

	@GetMapping("readAllServices")
	public String serviceView(Model model) {

		List<ServiceAddForm> listofservice = serviceFormService.readAllServices();
		model.addAttribute("allservices", listofservice);
		return "admin/servicereadupdate";
	}

	@GetMapping("deleteService/{id}")
	public String deleteServiceById(@PathVariable int id, RedirectAttributes redirectAttributes) {

		try {

			boolean deleted = serviceFormService.deleteService(id);

			if (deleted) {

				redirectAttributes.addFlashAttribute("msg", "Service Deleted Successfully");

			} else {
				redirectAttributes.addFlashAttribute("msg", "Service Not Found");

			}

		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("msg", "Something Went Wrong");
		}

		return "redirect:/admin/readAllServices";

	}

	@GetMapping("editService/{id}")
	public String editService(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {

		ServiceAddForm service = serviceFormService.getById(id);

		if (service == null) {
			redirectAttributes.addFlashAttribute("msg", "Service Not Found");
			return "redirect:/admin/readAllServices";
		}

		model.addAttribute("service", service);
		return "admin/editservice";
	}

	@PostMapping("updateService")
	public String updateService(@ModelAttribute ServiceAddForm serviceAddForm,
			@RequestParam("imageFile") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {

		try {
			boolean updated = serviceFormService.updateService(serviceAddForm, multipartFile);

			if (updated) {
				redirectAttributes.addFlashAttribute("msg", "Service Updated Successfully");
			} else {
				redirectAttributes.addFlashAttribute("msg", "Service Not Found");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "Something Went Wrong");
		}

		return "redirect:/admin/readAllServices";
	}

	@PostMapping("updateCredentials")
	public String changeCredentials(@RequestParam("oldusername") String oldusername,
			@RequestParam("oldpassword") String oldpassword, @RequestParam("newusername") String newusername,
			@RequestParam("newpassword") String newpassword, RedirectAttributes redirectAttributes

	) {

		String result = adminCredentialService.checkAdminCredential(oldusername, oldpassword);
		if (result.equals("SUCCESS")) {

			result = adminCredentialService.updateAdminCredential(newusername, newpassword, oldusername);
			redirectAttributes.addFlashAttribute("message", result);
			return "redirect:/admin/changeCredentials";

		} else {
			redirectAttributes.addFlashAttribute("message", result);
			return "redirect:/admin/changeCredentials";
		}

	}

	/*
	 ***********************  CAR Related********************/
    
	@GetMapping("addCar")
	public String addcarView() {

		return "admin/addcar";
	}

	@InitBinder
	public void stopBindingImage(WebDataBinder webDataBinder) {

		webDataBinder.setDisallowedFields("carImage");
	}

	@PostMapping("addCar")
	public String addNewCar(@ModelAttribute CarAddForm carAddForm,
			@RequestParam("carImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {

		String originalFilename = multipartFile.getOriginalFilename();
		carAddForm.setCarImage(originalFilename);
		try {

			CarAddForm car = carFormService.addCar(carAddForm, multipartFile);

			if (car != null) {

				redirectAttributes.addFlashAttribute("msg", "Car Added Successfully");

			} else {

				redirectAttributes.addFlashAttribute("msg", "SomeThing Went Wrong");

			}
		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("msg", "SomeThing Went Wrong");
		}
		return "redirect:/admin/addCar";
	}
	
	
	

	@GetMapping("updateDeleteCar")
	public String updateandDeleteCar(Model model) {

		List<CarAddForm> allCars = carFormService.readAllCars();
		model.addAttribute("cars", allCars);
		return "admin/updateanddelete-car";
	}
	
	

	@GetMapping("editCar/{id}")
	public String editCar(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {

		 CarAddForm carAddForm = carFormService.readCar(id).get();

		if (carAddForm == null) {
			redirectAttributes.addFlashAttribute("msg", "Car Not Found");
			return "redirect:/admin/updateanddelete-car";
		}

		model.addAttribute("car", carAddForm);
		return "admin/editcar";
	}
	
	
	@InitBinder
	public void stopBindingImg(WebDataBinder webDataBinder) {

		webDataBinder.setDisallowedFields("carImage");
	}

	@PostMapping("updateCar")
	public String updateCar(@ModelAttribute CarAddForm carAddForm,
			@RequestParam("carImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {

		try {
		
			boolean updated = carFormService.updateCar(carAddForm, multipartFile);

		if (updated) {
				redirectAttributes.addFlashAttribute("msg", "Car Updated Successfully");
			} else {
				redirectAttributes.addFlashAttribute("msg", "Car Not Found");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("msg", "Something Went Wrong");
		}

		return "redirect:/admin/updateDeleteCar";
	}
	
	@GetMapping("deleteCar/{id}")
	public String deleteCarById(@PathVariable int id, RedirectAttributes redirectAttributes) {

		try {

			boolean deleted= carFormService.deleteCar(id);

			if (deleted) {

				redirectAttributes.addFlashAttribute("msg", "Car Deleted Successfully");

			} else {
				redirectAttributes.addFlashAttribute("msg", "Car Not Found");

			}

		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("msg", "Something Went Wrong");
		}

		return "redirect:/admin/updateDeleteCar";

	}
	

	
	@GetMapping("cancelCarBooking/{id}")
	public String cancelCarBooking(@PathVariable int id, RedirectAttributes redirectAttributes) {
	    try {
	        bookingFormService.cancelCarBooking(id);
	        redirectAttributes.addFlashAttribute("message", "BOOKING CANCELLED & CAR SET TO AVAILABLE");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("message", "ERROR DURING CANCELLATION");
	    }
	    return "redirect:/admin/readAllBookings";
	}
}
