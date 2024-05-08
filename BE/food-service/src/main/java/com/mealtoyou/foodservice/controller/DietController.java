package com.mealtoyou.foodservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.foodservice.application.dto.DietFoodRequestDto;
import com.mealtoyou.foodservice.application.service.DietService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/diets")
public class DietController {

	private final DietService dietService;

	private long toUserId(String token) {
		return 1;
	}

	@PostMapping
	public Mono<ResponseEntity<Void>> createDiet(
		@RequestHeader("Authorization") String token,
		@RequestBody List<DietFoodRequestDto> dietFoodRequestDtoList
	) {
		return dietService.createDiet(toUserId(token), dietFoodRequestDtoList)
			.then(Mono.just(ResponseEntity.ok().build()));
	}

}
