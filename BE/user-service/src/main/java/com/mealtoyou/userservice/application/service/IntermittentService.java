package com.mealtoyou.userservice.application.service;

import org.springframework.stereotype.Service;

import com.mealtoyou.userservice.application.dto.request.IntermittentRequestDto;
import com.mealtoyou.userservice.domain.model.Intermittent;
import com.mealtoyou.userservice.domain.repository.IntermittentRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IntermittentService {
	private final JwtTokenProvider jwtTokenProvider;
	private final IntermittentRepository intermittentRepository;

	public Mono<String> updateTime(String token, IntermittentRequestDto intermittentRequestDto) {
		Long userId = jwtTokenProvider.getUserId(token);
		return intermittentRepository.findByUserId(userId)
			.flatMap(intermittent -> {
				intermittent.updateTime(intermittentRequestDto);
				return intermittentRepository.save(intermittent);
			}).switchIfEmpty(Mono.defer(() -> {
				Intermittent newIntermittent = Intermittent.builder()
					.userId(userId)
					.startTime(intermittentRequestDto.getStartTime())
					.endTime(intermittentRequestDto.getEndTime())
					.build();
				return intermittentRepository.save(newIntermittent);
			})).then(Mono.just("저장 성공")).onErrorResume(e -> Mono.error(new RuntimeException()));

	}
}
