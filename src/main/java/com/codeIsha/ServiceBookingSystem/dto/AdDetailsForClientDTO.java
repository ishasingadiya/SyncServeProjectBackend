package com.codeIsha.ServiceBookingSystem.dto;
import java.util.List;
import lombok.Data;
@Data
public class AdDetailsForClientDTO {
	private AdDTO adDTO;
	private List<ReviewDTO> reviewDTOList;
	public AdDTO getAdDTO() {
		return adDTO;
	}
	public void setAdDTO(AdDTO adDTO) {
		this.adDTO = adDTO;
	}
	public List<ReviewDTO> getReviewDTOList() {
		return reviewDTOList;
	}
	public void setReviewDTOList(List<ReviewDTO> reviewDTOsList) {
		this.reviewDTOList = reviewDTOsList;
	}
}
