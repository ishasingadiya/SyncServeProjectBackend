package com.codeIsha.ServiceBookingSystem.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Ensure this import is present
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeIsha.ServiceBookingSystem.Email.EmailService;
import com.codeIsha.ServiceBookingSystem.dto.AdDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReservationDTO;
import com.codeIsha.ServiceBookingSystem.entity.Inquiry;
import com.codeIsha.ServiceBookingSystem.entity.ServiceRequest;
import com.codeIsha.ServiceBookingSystem.repository.InquiryRepository;
import com.codeIsha.ServiceBookingSystem.repository.ServiceRequestRepository;
import com.codeIsha.ServiceBookingSystem.services.client.ClientService;
import com.codeIsha.ServiceBookingSystem.services.company.CompanyService;
import com.codeIsha.ServiceBookingSystem.dto.contactDTO;
@RestController
@RequestMapping("/api/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    @Autowired
    private InquiryRepository inquiryRepository;
    @Autowired
    private EmailService emailService;
    @PostMapping("/ad/{userId}")
    public ResponseEntity<?> postAd(@PathVariable Long userId, @ModelAttribute AdDTO adDTO) throws IOException {
        boolean success = companyService.postAd(userId, adDTO);
        System.out.printf("Posted Ad: %s\n", adDTO);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/ads/user/{userId}")
    public ResponseEntity<?> getAllAdsByUserId(@PathVariable Long userId) {
        System.out.printf("Fetching all ads for user ID: %d\n", userId);
        return ResponseEntity.ok(companyService.getAllAds(userId));
    }
    @GetMapping("/ad/{adId}")
    public ResponseEntity<?> getAdById(@PathVariable Long adId) {
        AdDTO adDTO = companyService.getAdById(adId);
        System.out.printf("Fetching ad with ID: %d\n", adId);
        System.out.printf("Ad Details: %s\n", adDTO);
        return adDTO != null ? ResponseEntity.ok(adDTO) : ResponseEntity.notFound().build();
    }
    @PutMapping("/ad/{adId}")  
    public ResponseEntity<?> updateAd(@PathVariable Long adId, @ModelAttribute AdDTO adDTO) throws IOException {
        boolean success = companyService.updateAd(adId, adDTO);
        System.out.printf("Updated Ad: %s\n", adDTO);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/ad/{adId}")
    public ResponseEntity<?> deleteAd(@PathVariable Long adId){
    	boolean success = companyService.deleteAd(adId);
    	if(success) {
    		return ResponseEntity.status(HttpStatus.OK).build();
    	}else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    }
    @GetMapping("/bookings/{companyId}")
    public ResponseEntity<List<ReservationDTO>> getAllAdBookings(@PathVariable Long companyId){
    	return ResponseEntity.ok(companyService.getAllAdBookings(companyId));
    }
    @GetMapping("/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status){
    	boolean success = companyService.changeBookingStatus(bookingId, status);
    	if(success) return ResponseEntity.ok().build();
    	return ResponseEntity.notFound().build();
    } 
    @GetMapping("/service-requests")
    public ResponseEntity<?> getAllServiceRequests() {
        return ResponseEntity.ok(serviceRequestRepository.findAll());
    }
    @PutMapping("/service-request/{id}/respond")
    public ResponseEntity<?> respondToServiceRequest(@PathVariable Long id, @RequestBody String response) {
        Optional<ServiceRequest> optionalRequest = serviceRequestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            ServiceRequest request = optionalRequest.get();
            request.setResponse(response);
            serviceRequestRepository.save(request);

            String subject = "Service Request Status Update";
            String body = "<div style='font-family: Arial, sans-serif; color: #333;'>"
                          + "<p>Dear Client,</p>"
                          + "<p>Your service request has been updated with the following response:</p>"
                          + "<blockquote style='background: #f8f9fa; padding: 10px; border-left: 5px solid #007bff;'>"
                          + "<b>" + response + "</b>"
                          + "</blockquote>"
                          + "<p>For more details, click the button below:</p>"
                          + "<a href='http://localhost:4200/client/bookings/" + id + "' "
                          + "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; "
                          + "background: #007bff; text-decoration: none; border-radius: 5px;'>"
                          + "View Service Request</a>"
                          + "<p>Thank you for using <b>SyncServe</b>!</p>"
                          + "<hr style='border: none; border-top: 1px solid #ccc;'>"
                          + "<p style='font-size: 12px; color: #777;'>"
                          + "If you have any questions, please contact our support team."
                          + "</p>"
                          + "</div>";

            emailService.sendEmail(request.getEmail(), subject, body);
            return ResponseEntity.ok("Response updated and email sent.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Request not found.");
        }
    }

    @PutMapping("/inquiry/{id}/respond")
    public ResponseEntity<?> respondToInquiry(@PathVariable Long id, @RequestBody String response) {
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);
        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();
            inquiry.setResponse(response);
            inquiryRepository.save(inquiry);
            return ResponseEntity.ok("Response updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquiry not found.");
        }
    }
    @GetMapping("/inquiries")
    public ResponseEntity<?> getAllInquiries() {
        return ResponseEntity.ok(inquiryRepository.findAll());
    }

}
