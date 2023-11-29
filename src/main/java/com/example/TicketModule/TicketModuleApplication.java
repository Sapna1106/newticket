package com.example.TicketModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketModuleApplication.class, args);
		System.out.println("start");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
