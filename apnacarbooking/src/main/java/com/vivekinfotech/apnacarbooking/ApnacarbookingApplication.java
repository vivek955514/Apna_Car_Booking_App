package com.vivekinfotech.apnacarbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync   //This allows tasks to run in the background
public class ApnacarbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApnacarbookingApplication.class, args);
	}

}
