package com.mealtoyou.userservice.application.service;

import com.mealtoyou.userservice.application.dto.OAuth2UserInfo;
import com.mealtoyou.userservice.application.dto.PrincipalDetails;
import com.mealtoyou.userservice.domain.model.User;
import com.mealtoyou.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;  // 사용자 저장소
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;  // OAuth2 클라이언트 등록 저장소
    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;  // OAuth2 클라이언트 인증 정보 저장소

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // WebClient를 생성하여 OAuth2 사용자 정보 엔드포인트에 요청을 보내기 위한 준비
        WebClient webClient = WebClient.builder()
                .apply(oauth2Configuration(userRequest))  // WebClient에 OAuth2 토큰을 추가하는 필터 적용
                .build();

        // OAuth2 사용자 정보 엔드포인트에 GET 요청을 보내고, 응답을 JSON으로 받음
        return webClient.get()
                .uri(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                // 응답 본문을 Map<String, Object> 형식으로 변환
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(userInfo -> {
                    // OAuth2 사용자 정보를 DTO로 변환
                    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(userRequest.getClientRegistration().getRegistrationId(), userInfo);
                    // 사용자 정보를 가져오거나 저장하여 PrincipalDetails를 생성하고 반환
                    return getOrSave(oAuth2UserInfo)
                            .map(user -> new PrincipalDetails(user, userInfo, userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()));
                });
    }

    // WebClient에 OAuth2 토큰을 추가하는 필터 생성
    private Consumer<WebClient.Builder> oauth2Configuration(OAuth2UserRequest userRequest) {
        return builder -> builder.filter(new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository))
                .defaultRequest(request -> request.headers(headers -> headers.setBearerAuth(userRequest.getAccessToken().getTokenValue())));
    }

    // 사용자 정보를 가져오거나 저장하는 메서드
    private Mono<User> getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        // 이메일을 기반으로 사용자를 찾음
        return userRepository.findByEmail(oAuth2UserInfo.email())
                // 사용자가 존재하지 않으면 새로운 사용자로 저장
                .switchIfEmpty(Mono.defer(() -> Mono.just(oAuth2UserInfo.toEntity())
                        .flatMap(userRepository::save)));
    }
}