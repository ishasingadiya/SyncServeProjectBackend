package com.codeIsha.ServiceBookingSystem.services.client;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.codeIsha.ServiceBookingSystem.dto.AdDTO;
import com.codeIsha.ServiceBookingSystem.dto.AdDetailsForClientDTO;
import com.codeIsha.ServiceBookingSystem.dto.InquiryDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReservationDTO;
import com.codeIsha.ServiceBookingSystem.dto.ReviewDTO;
import com.codeIsha.ServiceBookingSystem.dto.ServiceRequestDto;
import com.codeIsha.ServiceBookingSystem.dto.contactDTO;
import com.codeIsha.ServiceBookingSystem.entity.Ad;
import com.codeIsha.ServiceBookingSystem.entity.Inquiry;
import com.codeIsha.ServiceBookingSystem.entity.Reservation;
import com.codeIsha.ServiceBookingSystem.entity.Review;
import com.codeIsha.ServiceBookingSystem.entity.ServiceRequest;
import com.codeIsha.ServiceBookingSystem.entity.User;
import com.codeIsha.ServiceBookingSystem.entity.contact;
import com.codeIsha.ServiceBookingSystem.enums.ReservationStatus;
import com.codeIsha.ServiceBookingSystem.enums.ReviewStatus;
import com.codeIsha.ServiceBookingSystem.repository.AdRepository;
import com.codeIsha.ServiceBookingSystem.repository.InquiryRepository;
import com.codeIsha.ServiceBookingSystem.repository.ReservationRepository;
import com.codeIsha.ServiceBookingSystem.repository.ReviewRepository;
import com.codeIsha.ServiceBookingSystem.repository.ServiceRequestRepository;
import com.codeIsha.ServiceBookingSystem.repository.UserRepository;
import com.codeIsha.ServiceBookingSystem.repository.contactRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private AdRepository adRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    
    @Autowired
    private InquiryRepository inquiryRepository;
    
    @Autowired
    private contactRepository contactRepository;
    @Override
    public List<AdDTO> getAllAds() {
        return adRepository.findAll().stream().map(Ad::getAdDto).collect(Collectors.toList());
    }
    
    @Override
    public List<AdDTO> searchAdByName(String name){
        return adRepository.findAllByServiceNameContaining(name).stream().map(Ad::getAdDto).collect(Collectors.toList());
    }
    
    public boolean bookService(ReservationDTO reservationDTO) {
    	Optional<Ad> optionalAd = adRepository.findById(reservationDTO.getAdId());
    	Optional<User> optionalUser = userRepository.findById(reservationDTO.getUserId());
    	
    	if(optionalAd.isPresent() && optionalUser.isPresent()) {
    		Reservation reservation = new Reservation();
    		
    		reservation.setBookDate(reservationDTO.getBookDate());
    		reservation.setReservationStatus(ReservationStatus.PENDING);
    		reservation.setUser(optionalUser.get());
    		
    		reservation.setAd(optionalAd.get());
    		reservation.setCompany(optionalAd.get().getUser());
    		reservation.setReviewStatus(ReviewStatus.FALSE);
    		
    		reservationRepository.save(reservation);
    		return true;
    		
    	}
    	return false;
    }
    
    @Override
    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId) {
    	Optional<Ad> optionalAd = adRepository.findById(adId);
    	AdDetailsForClientDTO adDetailsForClientDTO = new AdDetailsForClientDTO();
    	if(optionalAd.isPresent()) {
    		adDetailsForClientDTO.setAdDTO(optionalAd.get().getAdDto());
    		
    		List<Review> reviewList = reviewRepository.findAllByAdId(adId);
    		adDetailsForClientDTO.setReviewDTOList(reviewList.stream().map(Review::getDto).collect(Collectors.toList()));
    		
    	}
    	return adDetailsForClientDTO;
    }
    
    public List<ReservationDTO> getAllBookingsByUserId(Long userId){
    	return reservationRepository.findAllByUserId(userId).stream().map(Reservation::getReservationDto).collect(Collectors.toList());
    }
    
    @Override
    public Boolean giveReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getUserId() == null || reviewDTO.getBookId() == null) {
            throw new IllegalArgumentException("User ID and Book ID must not be null");
        }
        
        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());
        Optional<Reservation> optionalBooking = reservationRepository.findById(reviewDTO.getBookId());
        
        if (optionalUser.isPresent() && optionalBooking.isPresent()) {
            Review review = new Review();
            
            review.setReviewDate(new Date());
            review.setReview(reviewDTO.getReview());
            review.setRating(reviewDTO.getRating());
            review.setUser(optionalUser.get());
            review.setAd(optionalBooking.get().getAd());
            
            reviewRepository.save(review);
            
            Reservation booking = optionalBooking.get();
            booking.setReviewStatus(ReviewStatus.TRUE);
            
            reservationRepository.save(booking);
            
            return true;
        }
        return false;
    }
    
    @Override
    public Boolean createServiceRequest(ServiceRequestDto serviceRequestDto) {
        if (serviceRequestDto.getName() == null || serviceRequestDto.getEmail() == null) {
            throw new IllegalArgumentException("Name and Email must not be null");
        }

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setName(serviceRequestDto.getName());
        serviceRequest.setEmail(serviceRequestDto.getEmail());
        serviceRequest.setRequestInfo(serviceRequestDto.getRequestInfo());
        serviceRequest.setDescription(serviceRequestDto.getDescription());
        serviceRequest.setAddress1(serviceRequestDto.getAddress1());
        serviceRequest.setAddress2(serviceRequestDto.getAddress2());
        serviceRequest.setCity(serviceRequestDto.getCity());
        serviceRequest.setState(serviceRequestDto.getState());
        serviceRequest.setZip(serviceRequestDto.getZip());
        serviceRequest.setMobile(serviceRequestDto.getMobile());
        serviceRequest.setDate(serviceRequestDto.getDate());

        serviceRequestRepository.save(serviceRequest);

        return true;
    }
    
    public List<InquiryDTO> getInquiriesByUser(Long userId) {
        return inquiryRepository.findAllByUserId(userId)
                .stream()
                .map(inquiry -> {
                    InquiryDTO dto = new InquiryDTO();
                    dto.setName(inquiry.getName());
                    dto.setEmail(inquiry.getEmail());
                    dto.setInquiryType(inquiry.getInquiryType());
                    dto.setDescription(inquiry.getDescription());
                    dto.setResponse(inquiry.getResponse());
                    dto.setUserId(inquiry.getUserId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public boolean submitContactForm(contactDTO contactDTO) {
        contact contact = new contact();
        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        contact.setEmail(contactDTO.getEmail());
        contact.setMessage(contactDTO.getMessage());
        
        contactRepository.save(contact);
        return true;
    }
}
