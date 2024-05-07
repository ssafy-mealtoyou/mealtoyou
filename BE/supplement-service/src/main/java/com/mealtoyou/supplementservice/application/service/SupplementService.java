package com.mealtoyou.supplementservice.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mealtoyou.supplementservice.application.dto.SupplementRequestDto;
import com.mealtoyou.supplementservice.application.dto.SupplementResponseDto;
import com.mealtoyou.supplementservice.domain.model.Supplement;
import com.mealtoyou.supplementservice.domain.repository.SupplementRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SupplementService {
	private final SupplementRepository supplementRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public Mono<String> registerSupplements(String token, List<SupplementRequestDto> supplementRequestDtos) {
		Long userId = jwtTokenProvider.getUserId(token);

		Flux<Supplement> supplementsFlux = Flux.fromIterable(supplementRequestDtos)
			.flatMap(dto -> supplementRepository.findByUserIdAndName(userId, dto.getName())
				.flatMap(existingSupplement -> {
					// 존재하는 Supplement 업데이트
					existingSupplement.toBuilder()
						.userId(userId)
						.takedYn(dto.getTakedYn())
						.alertTime(dto.getAlertTime())
						.name(existingSupplement.getName())
						.build();
					return supplementRepository.save(existingSupplement);
				})
				.switchIfEmpty(Mono.defer(() -> {

					Supplement newSupplement = Supplement.builder()
						.userId(userId)
						.name(dto.getName())
						.takedYn(dto.getTakedYn())
						.alertTime(dto.getAlertTime())
						.build();
					return supplementRepository.save(newSupplement);
				}))
			);

		return supplementsFlux.then(Mono.just("Supplements registered successfully"));
	}

	public Flux<SupplementResponseDto> getSupplements(String token) {
		Long userId = jwtTokenProvider.getUserId(token);

		return supplementRepository.findByUserId(userId)
			.map(supplement -> SupplementResponseDto.builder()
				.supplementId(supplement.getSupplementId())
				.name(supplement.getName())
				.takedYn(supplement.getTakedYn())
				.alertTime(supplement.getAlertTime())
				.build());
	}
}
