package com.codeIsha.ServiceBookingSystem.services.client;

import java.util.List;

import com.codeIsha.ServiceBookingSystem.dto.AdDTO;
import com.codeIsha.ServiceBookingSystem.dto.AdDetailsForClientDTO;
import com.codeIsha.ServiceBookingSystem.dto.InquiryDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReservationDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReviewDTO;
import com.codeIsha.ServiceBookingSystem.dto.ServiceRequestDto;
import com.codeIsha.ServiceBookingSystem.dto.contactDTO;

import jakarta.mail.MessagingException;

public interface ClientService {

	List<AdDTO> getAllAds();
	
	List<AdDTO> searchAdByName(String name);
	
	boolean bookService(ReservationDTO reservationDTO);
	
	AdDetailsForClientDTO getAdDetailsByAdId(Long adId);
	
	List<ReservationDTO> getAllBookingsByUserId(Long userId);
	
	Boolean giveReview(ReviewDTO reviewDTO);
	
	Boolean createServiceRequest(ServiceRequestDto serviceRequestDto);
	
	List<InquiryDTO> getInquiriesByUser(Long userId);
	
	public boolean submitContactForm(contactDTO contactDTO);
	 
 }
