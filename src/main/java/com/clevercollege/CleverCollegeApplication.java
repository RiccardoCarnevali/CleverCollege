package com.clevercollege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CleverCollegeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleverCollegeApplication.class, args);
	}
	
}
