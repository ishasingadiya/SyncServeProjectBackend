package com.codeIsha.ServiceBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeIsha.ServiceBookingSystem.entity.contact;

@Repository
public interface contactRepository extends JpaRepository<contact, Long>{

}
