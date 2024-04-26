package com.mealtoyou.userservice.infrastructure.config;

import com.mealtoyou.userservice.application.service.CustomOAuth2UserService;
import com.mealtoyou.userservice.infrastructure.handler.CustomOAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
//    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint 를 열어줘야 함
                .requestMatchers("/error");
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> corsConfigurationSource())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/**")
                        .permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(oauth -> oauth
                        .authenticationSuccessHandler(authenticationSuccessHandler())
                        .authenticationFailureHandler((webFilterExchange, exception) ->
                                Mono.fromRunnable(() ->
                                        webFilterExchange.getExchange().getResponse()
                                                .setStatusCode(HttpStatus.UNAUTHORIZED))

                        )
                        .clientRegistrationRepository(clientRegistrationRepository)
                        .authenticationMatcher(new PathPatternParserServerWebExchangeMatcher("/login/oauth2/code/**"))
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((webFilterExchange, accessDeniedException) ->
                                Mono.fromRunnable(() ->
                                        webFilterExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                        )
                );

        return http.build();
    }

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler() {
        return customOAuth2SuccessHandler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("https://j10a102.p.ssafy.io");
        config.addAllowedHeader("*"); // header
        config.addAllowedMethod("*"); // method
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
