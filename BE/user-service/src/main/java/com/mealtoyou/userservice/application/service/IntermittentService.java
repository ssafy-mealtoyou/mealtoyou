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

		Mono<Void> updateUserMono = userRepository.findById(userId)
			.flatMap(user -> {
				if (!user.isIntermittentFasting()) {
					user.updateIntermittent();
					return userRepository.save(user).then();
				}
				return Mono.empty();
			});

		updateUserMono.then(intermittentRepository.findByUserId(userId)
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
			})).then(Mono.just("저장 성공"))
		).subscribe();
		return Mono.just("저장 성공");
	}
}
