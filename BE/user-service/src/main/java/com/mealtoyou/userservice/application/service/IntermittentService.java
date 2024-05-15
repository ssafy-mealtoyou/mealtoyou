package com.mealtoyou.userservice.application.service;

import org.springframework.stereotype.Service;

import com.mealtoyou.userservice.application.dto.request.IntermittentRequestDto;
import com.mealtoyou.userservice.domain.model.Intermittent;
import com.mealtoyou.userservice.domain.repository.IntermittentRepository;
import com.mealtoyou.userservice.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IntermittentService {
	private final JwtTokenProvider jwtTokenProvider;
	private final IntermittentRepository intermittentRepository;
	private final UserRepository userRepository;

	public Mono<String> updateTime(String token, IntermittentRequestDto intermittentRequestDto) {
		Long userId = jwtTokenProvider.getUserId(token);
		return userRepository.findById(userId)
			.flatMap(user -> {
				boolean shouldUpdateUser = intermittentRequestDto.getIntermittentYn() != user.isIntermittentFasting();
				if (shouldUpdateUser) {
					user.updateIntermittent(intermittentRequestDto.getIntermittentYn());
					return userRepository.save(user).then(updateIntermittentInfo(userId, intermittentRequestDto));
				} else {
					return updateIntermittentInfo(userId, intermittentRequestDto);
				}
			})
			.thenReturn("저장 성공");
	}

	private Mono<Void> updateIntermittentInfo(Long userId, IntermittentRequestDto intermittentRequestDto) {
		if (!intermittentRequestDto.getIntermittentYn()) {
			return intermittentRepository.findByUserId(userId).flatMap(intermittentRepository::delete).then();
		}
		return intermittentRepository.findByUserId(userId)
			.flatMap(intermittent -> {
				intermittent.updateTime(intermittentRequestDto);
				return intermittentRepository.save(intermittent);
			})
			.switchIfEmpty(Mono.defer(() -> {
				Intermittent newIntermittent = Intermittent.builder()
					.userId(userId)
					.startTime(intermittentRequestDto.getStartTime())
					.endTime(intermittentRequestDto.getEndTime())
					.build();
				return intermittentRepository.save(newIntermittent);
			}))
			.then();
	}

	// public Mono<Map<String, String>> getIntermittent(String token) {
	// 	Long userId = jwtTokenProvider.getUserId(token);
	// 	return userRepository.findById(userId).flatMap(user -> {
	// 		if (user.isIntermittentFasting()) {
	// 			return intermittentRepository.findByUserId(userId)
	// 				.map(intermittent -> Map.of("intermittentYn", "true",
	// 					"startTime", intermittent.getStartTime().toString(),
	// 					"endTime", intermittent.getEndTime().toString()));
	// 		} else {
	// 			return Mono.just(Map.of("intermittentYn", "false", "startTime", "00:00", "endTime", "00:00"));
	// 		}
	// 	});
	// }

}
