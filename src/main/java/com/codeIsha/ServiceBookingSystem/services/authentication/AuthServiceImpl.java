package com.codeIsha.ServiceBookingSystem.services.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codeIsha.ServiceBookingSystem.Email.EmailService;
import com.codeIsha.ServiceBookingSystem.dto.SignupRequestDTO;
import com.codeIsha.ServiceBookingSystem.dto.UserDto;
import com.codeIsha.ServiceBookingSystem.entity.User;
import com.codeIsha.ServiceBookingSystem.enums.UserRole;
import com.codeIsha.ServiceBookingSystem.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Override
	public UserDto signupClient(SignupRequestDTO signupRequestDTO) {
		return signupUser(signupRequestDTO, UserRole.CLIENT);
	}

	@Override
	public UserDto signupCompany(SignupRequestDTO signupRequestDTO) {
		return signupUser(signupRequestDTO, UserRole.COMPANY);
	}

	private UserDto signupUser(SignupRequestDTO signupRequestDTO, UserRole role) {

	    if (presentByEmail(signupRequestDTO.getEmail())) {
	        logger.warn("Signup attempt with existing email: {}", signupRequestDTO.getEmail());
	        throw new RuntimeException("Email already exists.");
	    }

	    User user = new User();
	    user.setName(signupRequestDTO.getName());
	    user.setLastname(signupRequestDTO.getLastname());
	    user.setEmail(signupRequestDTO.getEmail());
	    user.setPhone(signupRequestDTO.getPhone());
	    user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
	    user.setRole(role);

	    User savedUser = userRepository.save(user);
	    logger.info("User registered successfully: {}", savedUser.getEmail());

	    // Welcome Email to User
	    String welcomeSubject = "🎉 Welcome to SyncServe – Get Started Today!";
	    String welcomeMessage = String.format(
	        "Dear %s,<br><br>"
	        + "Welcome to <strong>SyncServe</strong>! We're thrilled to have you onboard.<br><br>"
	        + "Here’s what you can do next:<br>"
	        + "✅ Complete your profile<br>"
	        + "✅ Explore available services<br>"
	        + "✅ Connect with professionals<br><br>"
	        + "If you have any questions, feel free to reach out to our support team.<br><br>"
	        + "Best Regards,<br>"
	        + "<strong>SyncServe Team</strong><br>"
	        + "<a href='http://localhost:4200/home'>Visit SyncServe</a>",
	        savedUser.getName()
	    );

	    Boolean emailSent = emailService.sendEmail(savedUser.getEmail(), welcomeSubject, welcomeMessage);

	    if (emailSent) {
	        logger.info("Welcome email sent successfully to: {}", savedUser.getEmail());
	    } else {
	        logger.warn("Failed to send welcome email to: {}", savedUser.getEmail());
	    }

	    // Admin Notification Email
	    String adminEmail = "nav@syncserve.com";
	    String adminSubject = "🚀 New User Registration: " + savedUser.getEmail();
	    String adminMessage = String.format(
	        "Hello Admin,<br><br>"
	        + "A new <strong>%s</strong> has registered on SyncServe.<br><br>"
	        + "<strong>User Details:</strong><br>"
	        + "👤 Name: %s %s<br>"
	        + "📧 Email: %s<br>"
	        + "📞 Phone: %s<br><br>"
	        + "You can review their profile and take necessary actions if needed.<br><br>"
	        + "Best Regards,<br>"
	        + "<strong>SyncServe System</strong>",
	        role.name().toLowerCase(),
	        savedUser.getName(),
	        savedUser.getLastname(),
	        savedUser.getEmail(),
	        savedUser.getPhone()
	    );

	    emailService.sendEmail(adminEmail, adminSubject, adminMessage);

	    return savedUser.getDto();
	}


	@Override
	public boolean presentByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
}
