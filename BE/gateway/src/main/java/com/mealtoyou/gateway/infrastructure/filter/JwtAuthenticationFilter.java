package com.mealtoyou.gateway.infrastructure.filter;

import com.mealtoyou.gateway.application.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("JwtAuthenticationFilter 시작");

        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.info("accessToken = {}", accessToken);
        String refreshToken = "";

        // 쿠키 가져오기
        List<String> cookies = exchange.getRequest().getHeaders().get("Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("refreshToken=")) {
                    refreshToken = cookie.substring("refreshToken=".length());
                }
            }
        }

        // 토큰 검사 생략(모두 허용 URL 의 경우 토큰 검사 통과
        if (!StringUtils.hasText(accessToken)) {
            return chain.filter(exchange);
        }

        log.info("token validation check start");
        if (!jwtTokenProvider.validateToken(accessToken)) {
            log.info("token 유효하지 않음");
            if (!refreshToken.isEmpty()) {
                log.info("refreshToken 검증");
                // 리프레시 토큰 만료시간 검증
                boolean validateRefreshToken = jwtTokenProvider.validateToken(refreshToken);
                // 리프레시 토큰 저장소 존재유무 확인
                boolean isRefreshToken = jwtTokenProvider.existsRefreshToken(refreshToken);
                if (validateRefreshToken && isRefreshToken) {
                    log.info("refreshToken 으로 accessToken 발급");
                    Long userId = jwtTokenProvider.getUserId(refreshToken);
                    String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
                    log.info("accessToken = {}", accessToken);
                    // 응답헤더에 어세스 토큰 추가
                    exchange.getResponse().getHeaders().add("Authorization", "Bearer " + newAccessToken);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                            new UsernamePasswordAuthenticationToken(userId, null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER")))));
                }
            }
        } else { // AccessToken 의 값이 있고, 유효한 경우에 진행
            log.info("token 유효");
            Long userId = jwtTokenProvider.getUserId(accessToken);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(
                    new UsernamePasswordAuthenticationToken(userId, null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER")))));
        }

        return chain.filter(exchange);
    }
}