package com.codeIsha.ServiceBookingSystem.entity;

import java.sql.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.codeIsha.ServiceBookingSystem.dto.ReservationDTO;
import com.codeIsha.ServiceBookingSystem.enums.ReservationStatus;
import com.codeIsha.ServiceBookingSystem.enums.ReviewStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reservationId;
	
	private ReservationStatus reservationStatus;
	
	private ReviewStatus reviewStatus;
	
	private Date bookDate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "company_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User company;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ad_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Ad ad;
	
	
	
	public Long getId() {
		return reservationId;
	}




	public void setId(Long id) {
		this.reservationId = id;
	}




	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}




	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}




	public ReviewStatus getReviewStatus() {
		return reviewStatus;
	}




	public void setReviewStatus(ReviewStatus reviewStatus) {
		this.reviewStatus = reviewStatus;
	}




	public Date getBookDate() {
		return bookDate;
	}




	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}




	public User getUser() {
		return user;
	}




	public void setUser(User user) {
		this.user = user;
	}




	public User getCompany() {
		return company;
	}




	public void setCompany(User company) {
		this.company = company;
	}




	public Ad getAd() {
		return ad;
	}




	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public ReservationDTO getReservationDto() {
		 ReservationDTO dto = new ReservationDTO();
		 
		 dto.setId(reservationId);
		 dto.setServiceName(ad.getServiceName());
		 dto.setBookDate(bookDate);
		 dto.setReservationStatus(reservationStatus);
		 dto.setReviewStatus(reviewStatus);
		 dto.setAdId(ad.getId());
		 dto.setCompanyId(company.getId());
		 dto.setUserName(user.getName());
		 
		 return dto;
	}
	
}
