package com.mealtoyou.foodservice;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class FoodServiceApplication {

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FoodServiceApplication.class, args);
	}

}
