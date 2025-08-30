package com.codeIsha.ServiceBookingSystem.controller;

import java.io.IOException;

import org.apache.catalina.connector.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.codeIsha.ServiceBookingSystem.dto.AuthenticationRequest;
import com.codeIsha.ServiceBookingSystem.dto.SignupRequestDTO;
import com.codeIsha.ServiceBookingSystem.dto.UserDto;
import com.codeIsha.ServiceBookingSystem.entity.User;
import com.codeIsha.ServiceBookingSystem.repository.UserRepository;
import com.codeIsha.ServiceBookingSystem.services.authentication.AuthService;
import com.codeIsha.ServiceBookingSystem.services.jwt.UserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            logger.warn("Signup attempt for existing client email: {}", signupRequestDTO.getEmail());
            return new ResponseEntity<>("Client already exists with this Email!", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdUser = authService.signupClient(signupRequestDTO);
        logger.info("Client registered successfully: {}", createdUser.getEmail());
        logger.info("Response sent for client signup: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            logger.warn("Signup attempt for existing company email: {}", signupRequestDTO.getEmail());
            return new ResponseEntity<>("Company already exists with this Email!", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdUser = authService.signupCompany(signupRequestDTO);
        logger.info("Company registered successfully: {}", createdUser.getEmail());
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws IOException {
        System.out.println("Received authentication request for user: " + authenticationRequest.getUsername());
        logger.info("Received request for user: {}", authenticationRequest.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            logger.info("Authentication successful for user: {}", authenticationRequest.getUsername());
        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {} - {}", authenticationRequest.getUsername(), e.getMessage());
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

        try {
            JSONObject jsonResponse = new JSONObject()
                .put("userId", user.getId())
                .put("role", user.getRole());
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            logger.error("JSON creation error for user: {}", user.getEmail(), e);
            return new ResponseEntity<>("Error creating JSON response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
