package com.wharehouse.sensormanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SensormanagementApplication {

	static void main(String[] args) {


		SpringApplication.run(SensormanagementApplication.class, args);
	}
	@Bean
	CommandLineRunner keepAlive() {
		return args -> {
			Thread.currentThread().join();
		};
	}
}
