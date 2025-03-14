package com.example.Connect4Api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Connect4ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Connect4ApiApplication.class, args);
	}

}
