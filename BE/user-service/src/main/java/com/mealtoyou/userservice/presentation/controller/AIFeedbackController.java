package com.mealtoyou.userservice.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.userservice.application.service.AIFeedbackService;
import com.mealtoyou.userservice.application.service.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AIFeedbackController {

	private final JwtTokenProvider jwtTokenProvider;
	private final AIFeedbackService aiFeedbackService;

	private long getUserId(String token) {
		return jwtTokenProvider.getUserId(token);
	}

	@GetMapping("/users/ai-feedback")
	public Mono<ResponseEntity<String>> getAIFeedback(
		@RequestHeader("Authorization") String token
	) {
		return aiFeedbackService.generateAIFeedback(getUserId(token))
			.map(ResponseEntity::ok);
	}

}
