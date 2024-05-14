package com.mealtoyou.userservice.application.service;

import org.springframework.stereotype.Service;

import com.mealtoyou.userservice.application.dto.request.FcmRequestDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FcmService {

	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public Mono<UserInfoResponseDto> updateFcmToken(String token, FcmRequestDto fcmRequestDto) {
		Long userId = jwtTokenProvider.getUserId(token);
		return userRepository
			.findById(userId).flatMap(user -> {
				user.updateUserFcmToken(fcmRequestDto);
				return userRepository.save(user);
			})
			.map(UserInfoResponseDto::fromEntity);
	}
}
