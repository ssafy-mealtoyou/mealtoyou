package com.mealtoyou.userservice.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.userservice.application.dto.request.FcmRequestDto;
import com.mealtoyou.userservice.application.service.FcmService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class FcmController {
	private final FcmService fcmService;

	@PutMapping("/users/fcm")
	public Mono<ResponseEntity<Void>> updateTime(@RequestHeader("Authorization") String token,
		@RequestBody FcmRequestDto fcmRequestDto) {
		return fcmService.updateFcmToken(token, fcmRequestDto).then(Mono.just(ResponseEntity.ok().build()));
	}
}
