package com.codeIsha.ServiceBookingSystem.services.authentication;

import com.codeIsha.ServiceBookingSystem.dto.SignupRequestDTO;
import com.codeIsha.ServiceBookingSystem.dto.UserDto;

public interface AuthService {
    UserDto signupClient(SignupRequestDTO signupRequestDTO);
    boolean presentByEmail(String email); 
    UserDto signupCompany(SignupRequestDTO signupRequestDTO);
}
