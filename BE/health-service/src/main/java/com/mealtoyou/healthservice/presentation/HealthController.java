package com.mealtoyou.healthservice.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.healthservice.application.dto.BodyDto;
import com.mealtoyou.healthservice.application.dto.ExerciseDto;
import com.mealtoyou.healthservice.application.service.BodyService;
import com.mealtoyou.healthservice.application.service.ExerciseService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {
	private final ExerciseService exerciseService;
	private final BodyService bodyService;

	@PostMapping("/exercise")
	public Mono<ResponseEntity<String>> saveExerciseData(@RequestHeader("Authorization") String token, @RequestBody
	ExerciseDto exerciseDto) {
		return exerciseService.saveExerciseData(token, exerciseDto)
			.map(response -> ResponseEntity.status(201).body(response)) // Handle successful save with 201 Created
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error saving exercise data: " + e.getMessage()))); // Handle errors
	}

	@GetMapping("/exercise")
	public ResponseEntity<Flux<ExerciseDto>> readExerciseData(@RequestHeader("Authorization") String token, @RequestParam(value="day", required = false, defaultValue="1") Integer day) {
		Flux<ExerciseDto> exerciseFlux = exerciseService.readExerciseData(token,day);
		return ResponseEntity.ok().body(exerciseFlux);
	}

	@PostMapping("/body-fat")
	public Mono<ResponseEntity<String>> saveBodyData(@RequestHeader("Authorization") String token, @RequestBody BodyDto bodyDto){
		return bodyService.saveBodyData(token,bodyDto)
			.map(response -> ResponseEntity.status(201).body(response))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error saving body data: " + e.getMessage())));
	}

	// @GetMapping("/body-fat")
	// public ResponseEntity<Flux<BodyDto>> readBodyData(@RequestHeader("Authorization") String token,@RequestParam(value="day", required = false, defaultValue="1")){
	//
	// }
}
