package com.mealtoyou.healthservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.healthservice.application.dto.ExerciseRequestDto;
import com.mealtoyou.healthservice.application.service.ExerciseService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {
	private final ExerciseService exerciseService;
	@PostMapping("/exercise")
	public Mono<ResponseEntity<String>> saveExerciseData(@RequestHeader("Authorization") String token, @RequestBody
		ExerciseRequestDto exerciseRequestDto) {
		return exerciseService.saveExerciseData(token, exerciseRequestDto)
			.map(response -> ResponseEntity.status(201).body(response)) // Handle successful save with 201 Created
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error saving exercise data: " + e.getMessage()))); // Handle errors
	}
}
