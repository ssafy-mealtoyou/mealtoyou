package com.mealtoyou.userservice.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.userservice.application.dto.request.IntermittentRequestDto;
import com.mealtoyou.userservice.application.service.IntermittentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class IntermittentController {
	private final IntermittentService intermittentService;

	@PutMapping("/users/intermittent")
	public Mono<ResponseEntity<Void>> updateTime(@RequestHeader("Authorization") String token,
		@RequestBody IntermittentRequestDto intermittentRequestDto) {
		return intermittentService.updateTime(token, intermittentRequestDto)
			.then(Mono.just(ResponseEntity.ok().build()));
	}
}