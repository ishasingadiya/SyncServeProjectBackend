package com.codeIsha.ServiceBookingSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codeIsha.ServiceBookingSystem.Email.EmailService;
import com.codeIsha.ServiceBookingSystem.dto.InquiryDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReservationDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReviewDTO;
import com.codeIsha.ServiceBookingSystem.dto.ServiceRequestDto;
import com.codeIsha.ServiceBookingSystem.dto.contactDTO;
import com.codeIsha.ServiceBookingSystem.entity.Inquiry;
import com.codeIsha.ServiceBookingSystem.repository.InquiryRepository;
import com.codeIsha.ServiceBookingSystem.services.client.ClientService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/client")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private InquiryRepository inquiryRepository;

	@Autowired
	private EmailService emailService;

	@GetMapping("/ads")
	public ResponseEntity<?> getAllAds() {
		return ResponseEntity.ok(clientService.getAllAds());
	}

	@GetMapping("/search/{name}")
	public ResponseEntity<?> searchAdByService(@PathVariable String name) {
		return ResponseEntity.ok(clientService.searchAdByName(name));
	}

	@PostMapping("/book-service")
	public ResponseEntity<?> bookService(@RequestBody ReservationDTO reservationDTO) {
		boolean success = clientService.bookService(reservationDTO);
		return success ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/ad/{adId}")
	public ResponseEntity<?> getAdDetailsByAdId(@PathVariable Long adId) {
		return ResponseEntity.ok(clientService.getAdDetailsByAdId(adId));
	}

	@GetMapping("/my-bookings/{userId}")
	public ResponseEntity<?> getAllBookingsByUserId(@PathVariable Long userId) {
		return ResponseEntity.ok(clientService.getAllBookingsByUserId(userId));

	}

	@PostMapping("/review")
	public ResponseEntity<?> giveReview(@RequestBody ReviewDTO reviewDTO) {
		boolean success = clientService.giveReview(reviewDTO);
		return success ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/service-request")
	public ResponseEntity<?> createServiceRequest(@RequestBody ServiceRequestDto serviceRequestDto) {
		boolean success = clientService.createServiceRequest(serviceRequestDto);

		if (success) {
			// Retrieve user email and company email
			String clientEmail = serviceRequestDto.getEmail();
			String companyEmail = "company@syncserve.com"; // Change to actual company email

			// Client Email
			String clientSubject = "🔔 Service Request Submitted Successfully!";
			String clientBody = "<html><body style='font-family: Arial, sans-serif; color: #333;'>" + "<h2>Dear "
					+ serviceRequestDto.getName() + ",</h2>"
					+ "<p>We have received your service request. Our team will review it and get back to you shortly.</p>"
					+ "<h3>📌 Request Details:</h3>" + "<table style='border-collapse: collapse; width: 100%;'>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Request Info:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getRequestInfo() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Description:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getDescription() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Address:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getAddress1() + ", " + serviceRequestDto.getCity() + ", "
					+ serviceRequestDto.getState() + " - " + serviceRequestDto.getZip() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Requested Date:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getDate() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Contact:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getMobile() + "</td></tr>" + "</table>"
					+ "<br><p><b>Track your request:</b></p>" + "<a href='http://localhost:4200/client/bookings' "
					+ "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; "
					+ "background: #007bff; text-decoration: none; border-radius: 5px;'>View Service Request</a>"
					+ "<br><br><p>Thank you for choosing <b>SyncServe</b>!</p>"
					+ "<p>Best Regards,<br><b>SyncServe Team</b></p>" + "</body></html>";

			emailService.sendEmail(clientEmail, clientSubject, clientBody);

			// Company Email
			String companySubject = "🚨 New Service Request Received";
			String companyBody = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
					+ "<h2>Hello Team,</h2>" + "<p>A new service request has been submitted. Here are the details:</p>"
					+ "<h3>📌 Client Details:</h3>" + "<table style='border-collapse: collapse; width: 100%;'>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Client Name:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getName() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Email:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getEmail() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Contact:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getMobile() + "</td></tr>" + "</table>" + "<h3>📌 Service Request Details:</h3>"
					+ "<table style='border-collapse: collapse; width: 100%;'>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Request Info:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getRequestInfo() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Description:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getDescription() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Address:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getAddress1() + ", " + serviceRequestDto.getCity() + ", "
					+ serviceRequestDto.getState() + " - " + serviceRequestDto.getZip() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Requested Date:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ serviceRequestDto.getDate() + "</td></tr>" + "</table>" + "<br><p><b>Manage this request:</b></p>"
					+ "<a href='http://localhost:4200/company/dashboard' "
					+ "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; "
					+ "background: #dc3545; text-decoration: none; border-radius: 5px;'>Manage Requests</a>"
					+ "<br><br><p>Best Regards,<br><b>SyncServe System</b></p>" + "</body></html>";

			emailService.sendEmail(companyEmail, companySubject, companyBody);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Service request created successfully. Confirmation email sent.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create service request.");
		}
	}

	@PostMapping("/inquiry")
	public ResponseEntity<?> submitInquiry(@RequestBody InquiryDTO inquiryDTO) {
		try {
			Inquiry inquiry = new Inquiry();
			inquiry.setName(inquiryDTO.getName());
			inquiry.setEmail(inquiryDTO.getEmail());
			inquiry.setResponse(inquiryDTO.getResponse());
			inquiry.setDescription(inquiryDTO.getDescription());
			// Removed setting ID unless it's required

			inquiryRepository.save(inquiry);

			// Send formatted email to the company
			String subject = "📩 New Inquiry Received from " + inquiryDTO.getName();
			String body = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
					+ "<h2 style='color: #007bff;'>📩 New Inquiry Received</h2>" + "<p>Dear Team,</p>"
					+ "<p>You have received a new inquiry. Please find the details below:</p>"
					+ "<table style='border-collapse: collapse; width: 100%;'>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Name:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ inquiryDTO.getName() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Email:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ inquiryDTO.getEmail() + "</td></tr>"
					+ "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Description:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>"
					+ inquiryDTO.getDescription() + "</td></tr>" + "</table>" + "<br><p><b>View all inquiries:</b></p>"
					+ "<a href='http://localhost:4200/company/inquiries' "
					+ "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; "
					+ "background: #007bff; text-decoration: none; border-radius: 5px;'>View Inquiries</a>"
					+ "<br><br><p>Best Regards,<br><b>SyncServe Team</b></p>" + "</body></html>";

			emailService.sendEmail("SyncServe@gmail.com", subject, body);

			return ResponseEntity.status(HttpStatus.CREATED).body("Inquiry submitted and email sent.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error submitting inquiry: " + e.getMessage());
		}
	}

	@GetMapping("/inquiries")
	public ResponseEntity<?> getAllInquiries() {
		try {
			List<Inquiry> inquiries = inquiryRepository.findAll();
			return inquiries.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No inquiries found.")
					: ResponseEntity.ok(inquiries);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving inquiries.");
		}
	}

	@PostMapping("/contact")
	public ResponseEntity<Map<String, String>> submitContactForm(@RequestBody contactDTO contactDTO) {
	    boolean success = clientService.submitContactForm(contactDTO);
	    if (success) {
	        // Email to Admin
	        String adminSubject = "📩 New Contact Form Submission";
	        String adminBody = "<html><body style='font-family: Arial, sans-serif; color: #333;'>"
	                + "<h2 style='color: #007bff;'>📩 New Contact Request</h2>"
	                + "<p>Dear Admin,</p>"
	                + "<p>You have received a new contact request. Please find the details below:</p>"
	                + "<table style='border-collapse: collapse; width: 100%;'>"
	                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Name:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>" 
	                + contactDTO.getFirstName() + " " + contactDTO.getLastName() + "</td></tr>"
	                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Email:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>" 
	                + contactDTO.getEmail() + "</td></tr>"
	                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'><b>Message:</b></td><td style='border: 1px solid #ddd; padding: 8px;'>" 
	                + contactDTO.getMessage() + "</td></tr>"
	                + "</table>"
	                + "<br><p>Best Regards,<br><b>SyncServe</b></p>"
	                + "</body></html>";

	        emailService.sendEmail("SyncServe@gmail.com", adminSubject, adminBody);

	        // JSON Response
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Contact form submitted successfully.");
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

	    // Error Response
	    Map<String, String> errorResponse = new HashMap<>();
	    errorResponse.put("message", "Failed to submit contact form.");
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}