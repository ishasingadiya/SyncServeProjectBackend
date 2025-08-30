package com.codeIsha.ServiceBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeIsha.ServiceBookingSystem.entity.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>{

    List<Ad> findAllByUserId(Long userId);
    List<Ad> findAllByServiceNameContaining(String name);
}
