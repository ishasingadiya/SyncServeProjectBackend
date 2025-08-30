package com.codeIsha.ServiceBookingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.codeIsha.ServiceBookingSystem" })
public class ServiceBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBookingSystemApplication.class, args);
	}
}