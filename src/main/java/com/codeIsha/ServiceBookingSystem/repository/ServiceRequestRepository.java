package com.codeIsha.ServiceBookingSystem.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.codeIsha.ServiceBookingSystem.entity.ServiceRequest;
@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long>{
 	List<ServiceRequest> findByUser_Id(@Param("userId") Long userId);
}
