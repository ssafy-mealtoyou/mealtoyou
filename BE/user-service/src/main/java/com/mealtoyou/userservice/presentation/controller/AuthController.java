package com.mealtoyou.userservice.presentation.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.userservice.application.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> body) {
		String idToken = body.get("idToken");
		return authService.validateIdToken(idToken)
			.flatMap(oidcDto -> {
				if (oidcDto != null) {
					return authService.generateTokenByEmail(oidcDto)
						.flatMap(generatedToken -> authService.saveTokens(generatedToken.getAccessToken(),
								generatedToken.getRefreshToken())
							.thenReturn(ResponseEntity.ok(Map.of("accessToken", generatedToken.getAccessToken(),
								"refreshToken", generatedToken.getRefreshToken()))));
				} else {
					// Mono.error를 반환하여 에러 처리
					return Mono.error(new RuntimeException("invalid error token"));
				}
			})
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(Map.of("error", "Invalid ID token"))));
	}

}
