package com.codeIsha.ServiceBookingSystem.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;
    @GetMapping("/send-mail")
    public String sendMail(@RequestParam String to) {
        emailService.sendEmail(to, "Test Email", "This is a test email from Spring Boot using Mailtrap.");
        return "Email sent successfully to " + to;
    }
}