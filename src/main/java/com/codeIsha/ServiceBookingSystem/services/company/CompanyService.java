package com.codeIsha.ServiceBookingSystem.services.company;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codeIsha.ServiceBookingSystem.dto.AdDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReservationDTO;
import com.codeIsha.ServiceBookingSystem.entity.Ad;

public interface CompanyService {

	boolean postAd(Long userId, AdDTO adDTO) throws IOException;
	
	List<AdDTO> getAllAds(Long userId);
	
	AdDTO getAdById(Long adId);
	
	boolean updateAd(Long adId, AdDTO adDTO) throws IOException;
	
	boolean deleteAd(Long adId);
	
	List<ReservationDTO> getAllAdBookings(Long companyId);
	
	boolean changeBookingStatus(Long bookingId, String status);
}