package com.mealtoyou.supplementservice.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.supplementservice.application.dto.SupplementRequestDto;
import com.mealtoyou.supplementservice.application.dto.SupplementResponseDto;
import com.mealtoyou.supplementservice.application.service.SupplementService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/supplements")
public class SupplementController {
	private final SupplementService supplementService;

	@PostMapping("")
	public Mono<ResponseEntity<String>> registerSupplements(@RequestHeader("Authorization") String token, @RequestBody List<SupplementRequestDto> supplements) {
		return supplementService.registerSupplements(token, supplements)
			.map(response -> ResponseEntity.status(201).body(response)) // Handle successful save with 201 Created
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error saving supplements data: " + e.getMessage()))); // Handle errors
	}

	@GetMapping
	public ResponseEntity<Flux<SupplementResponseDto>> getSupplements(@RequestHeader("Authorization") String token) {
		Flux<SupplementResponseDto> supplementResponseDtoFlux = supplementService.getSupplements(token);
		return ResponseEntity.ok().body(supplementResponseDtoFlux);

	}
}
