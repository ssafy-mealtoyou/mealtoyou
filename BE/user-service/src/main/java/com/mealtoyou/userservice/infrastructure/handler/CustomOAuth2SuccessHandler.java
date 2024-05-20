package com.mealtoyou.userservice.infrastructure.handler;

import com.mealtoyou.userservice.application.dto.GeneratedToken;
import com.mealtoyou.userservice.application.dto.RefreshToken;
import com.mealtoyou.userservice.application.service.JwtTokenProvider;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.RefreshTokenRepository;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("onAuthenticationSuccess start");
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

        // OAuth2User 로 캐스팅하여 인증된 사용자 정보를 가져온다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자 이메일을 가져온다.
        String email = oAuth2User.getAttribute("email");

        Mono<GeneratedToken> monoGeneratedToken = generateTokenByEmail(email);
        return monoGeneratedToken.flatMap(generatedToken ->
                handleTokens(response, generatedToken.getAccessToken(), generatedToken.getRefreshToken()));
    }

    public Mono<Void> handleTokens(ServerHttpResponse response, String accessToken, String refreshToken) {
        // 로그 출력
        log.info("accessToken = {}", accessToken);

        // 새로운 refreshToken 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(Duration.ofDays(30)) // 예시로 30일 유지
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();

        // response에 refreshToken 쿠키 추가
        response.addCookie(refreshTokenCookie);

        // 리프레시 토큰 레디스에 저장 (비동기로 실행됨)
        return Mono.fromRunnable(() -> {
                    refreshTokenRepository.save(new RefreshToken(refreshToken));
                }).subscribeOn(Schedulers.boundedElastic())
                .then(Mono.defer(() -> {
                    // 리다이렉트할 URL 생성
                    String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8082/user-service/google/callback?code=" + 1234)
                            .build()
                            .encode(StandardCharsets.UTF_8)
                            .toUriString();

                    // 리다이렉트
                    response.getHeaders().set(HttpHeaders.LOCATION, targetUrl);
                    response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                    response.setStatusCode(HttpStatus.FOUND);
                    return response.setComplete();
                }));
    }

    public Mono<GeneratedToken> generateTokenByEmail(String email) {
        return getUserIdByEmail(email)
                .flatMap(userId -> {
                    // userId를 이용하여 토큰을 생성하고 Mono<GeneratedToken>을 반환
                    return Mono.just(jwtTokenProvider.generateToken(userId));
                });
    }

    public Mono<Long> getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getUserId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found"))); // 사용자를 찾지 못한 경우 예외 발생
    }

}
