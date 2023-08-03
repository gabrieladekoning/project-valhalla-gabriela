package com.devoteam.accesscontrolservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = {"com.devoteam.library", "com.devoteam.accesscontrolservice"})
public class AccessControlServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessControlServiceApplication.class, args);
	}

}
