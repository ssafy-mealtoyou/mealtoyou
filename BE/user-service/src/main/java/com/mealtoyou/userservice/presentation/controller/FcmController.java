package com.mealtoyou.userservice.presentation.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.mealtoyou.userservice.application.dto.request.MessageRequestDto;
import com.mealtoyou.userservice.application.service.FcmService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class FcmController {
	private final FcmService fcmService;

	// fcm ( topic )
	@PostMapping("/message/fcm/topic")
	public Mono<ResponseEntity> sendMessageTopic(@RequestBody MessageRequestDto messageRequestDto) throws
		IOException,
		FirebaseMessagingException {
		fcmService.sendMessageByTopic(messageRequestDto.getTitle(), messageRequestDto.getBody());
		return Mono.just(ResponseEntity.ok().build());
	}

	// fcm ( token )
	@PostMapping("/message/fcm/token")
	public Mono<ResponseEntity> sendMessageToken(@RequestBody MessageRequestDto messageRequestDto) throws
		IOException,
		FirebaseMessagingException {
		fcmService.sendMessageByToken(messageRequestDto.getTitle(), messageRequestDto.getBody(),
			messageRequestDto.getTargetToken());
		return Mono.just(ResponseEntity.ok().build());
	}
}
