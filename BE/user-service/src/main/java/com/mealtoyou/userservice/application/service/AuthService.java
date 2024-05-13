package com.mealtoyou.userservice.application.service;

import org.springframework.stereotype.Service;

import com.mealtoyou.userservice.application.dto.GeneratedToken;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.RefreshTokenRepository;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import com.mealtoyou.userservice.infrastructure.util.OIDCDto;
import com.mealtoyou.userservice.infrastructure.util.VerifyUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public Mono<OIDCDto> validateIdToken(String idToken) {
		//IdToken검증 -> null 이나 OIDCDto 보냄
		return Mono.justOrEmpty(VerifyUtil.googleIdTokenVerify(idToken));
	}

	public Mono<Void> saveTokens(String accessToken, String refreshToken) {
		// 로그 출력
		log.info("accessToken = {}", accessToken);

		// TODO: 리프레시 토큰 레디스에 저장 (비동기로 실행됨)
		return Mono.fromRunnable(() -> {
				// refreshTokenRepository.save(new RefreshToken(refreshToken));
			}).subscribeOn(Schedulers.boundedElastic())
			.then();
	}

	public Mono<GeneratedToken> generateTokenByEmail(OIDCDto oidcDto) {
		return getOrSave(oidcDto)
			.flatMap(user -> {
				// userId를 이용하여 토큰을 생성하고 Mono<GeneratedToken>을 반환
				return Mono.just(jwtTokenProvider.generateToken(user.getUserId()));
			});
	}

	// 사용자 정보를 가져오거나 저장하는 메서드
	private Mono<User> getOrSave(OIDCDto oidcDto) {
		// 이메일을 기반으로 사용자를 찾음
		return userRepository.findByEmail(oidcDto.getEmail())
			// 사용자가 존재하지 않으면 새로운 사용자로 저장
			.switchIfEmpty(Mono.defer(() -> Mono.just(oidcDto.toEntity())
				.flatMap(userRepository::save)));
	}

}
