package com.codeIsha.ServiceBookingSystem.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.codeIsha.ServiceBookingSystem.entity.Inquiry;
import com.codeIsha.ServiceBookingSystem.entity.User;
@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByUserId(Long userId);
}