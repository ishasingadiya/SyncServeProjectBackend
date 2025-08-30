package com.codeIsha.ServiceBookingSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeIsha.ServiceBookingSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);
    
    Boolean existsByEmail(String email); 
}