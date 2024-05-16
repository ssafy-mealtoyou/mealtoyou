package com.mealtoyou.supplementservice.application.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
			.flatMap(dto -> {
				final LocalTime[] alertTime = new LocalTime[1];
				try {
					alertTime[0] = LocalTime.parse(dto.getAlertTime(), DateTimeFormatter.ofPattern("HH:mm"));
				} catch (DateTimeParseException e) {
					alertTime[0] = LocalTime.MIDNIGHT; // 기본값으로 자정 설정
				}

				return supplementRepository.findByUserIdAndName(userId, dto.getName())
					.flatMap(existingSupplement -> {
						// 존재하는 Supplement 업데이트
						Supplement updatedSupplement = existingSupplement.toBuilder()
							.userId(userId)
							.takenYn(dto.getTakenYn())
							.alertTime(alertTime[0]) // alertTime을 LocalTime으로 설정
							.name(existingSupplement.getName())
							.build();
						return supplementRepository.save(updatedSupplement);
					})
					.switchIfEmpty(Mono.defer(() -> {
						// 새 Supplement 생성
						Supplement newSupplement = Supplement.builder()
							.userId(userId)
							.name(dto.getName())
							.takenYn(dto.getTakenYn())
							.alertTime(alertTime[0]) // alertTime을 LocalTime으로 설정
							.build();
						return supplementRepository.save(newSupplement);
					}));
			});

		return supplementsFlux.then(Mono.just("Supplements registered successfully"));
	}

	public Flux<SupplementResponseDto> getSupplements(String token) {
		Long userId = jwtTokenProvider.getUserId(token);
		System.out.println(userId);
		return supplementRepository.findByUserId(userId)
			.map(supplement -> SupplementResponseDto.builder()
				.supplementId(supplement.getSupplementId())
				.name(supplement.getName())
				.takenYn(supplement.getTakenYn())
				.alertTime(supplement.getAlertTime())
				.build());
	}

	public Mono<String> deleteSupplement(String token, Long id) {
		return supplementRepository.deleteById(id)
			.then(Mono.just("Supplement deleted successfully"))
			.onErrorResume(e -> Mono.just("Failed to delete supplement: " + e.getMessage()));
	}

	public Mono<String> patchSupplement(String token, Long id) {
		Long userId = jwtTokenProvider.getUserId(token);
		return supplementRepository.findBySupplementId(id).flatMap(supplement -> {
				supplement.updateSupplementTakenYn(true);
				return supplementRepository.save(supplement);
			})
			.then(Mono.just("Supplement update successfully"))
			.onErrorResume(e -> Mono.just("Failed to update supplement: " + e.getMessage()));
	}

	public Mono<Void> resetTakenYn() {

		// 모든 영양제 조회
		Flux<Supplement> supplementsFlux = supplementRepository.findAll();

		// 모든 영양제의 takenYn 값을 false로 초기화
		return supplementsFlux.flatMap(supplement -> {
			// 빌더를 사용하여 객체 수정
			Supplement updatedSupplement = supplement.toBuilder()
				.takenYn(false)
				.build();
			return supplementRepository.save(updatedSupplement);
		}).then();
	}
}
