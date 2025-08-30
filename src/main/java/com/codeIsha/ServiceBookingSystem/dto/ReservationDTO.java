package com.codeIsha.ServiceBookingSystem.dto;
import java.sql.Date;
import java.util.List;
import com.codeIsha.ServiceBookingSystem.entity.ServiceRequest;
import com.codeIsha.ServiceBookingSystem.enums.ReservationStatus;
import com.codeIsha.ServiceBookingSystem.enums.ReviewStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Data
public class ReservationDTO {
	private Long reservationId;
	private Date bookDate;
	private String serviceName;
	private ReservationStatus reservationStatus;
	private ReviewStatus reviewStatus;
	private Long userId;
	private String userName;
	private Long companyId;
	private Long adId;
	public Long getId() {
		return reservationId;
	}
	public void setId(Long id) {
		this.reservationId = id;
	}
	public Date getBookDate() {
		return bookDate;
	}
	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getAdId() {
		return adId;
	}
	public void setAdId(Long adId) {
		this.adId = adId;
	}
	
}
