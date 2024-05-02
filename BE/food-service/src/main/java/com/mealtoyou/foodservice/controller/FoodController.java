package com.mealtoyou.foodservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.foodservice.application.dto.FoodDto;
import com.mealtoyou.foodservice.application.service.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/foods")
public class FoodController {
	private final FoodService foodService;

	@GetMapping
	public Mono<ResponseEntity<Flux<FoodDto>>> foodSearch(@RequestParam String keyword, String message) {
		return Mono.just(ResponseEntity.ok().body(foodService.getFoods(keyword)));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Flux<FoodDto>>> foodInfo(@PathVariable String id, String message) {
		return Mono.just(ResponseEntity.ok().body(foodService.ratioFood(0.0, 0.0, 0.0)));
	}

}
