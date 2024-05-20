package com.mealtoyou.userservice.presentation.controller;

import com.mealtoyou.userservice.application.dto.response.HealthInfoResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.mealtoyou.userservice.application.dto.request.UserGoalRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInbodyRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserWeightRequestDto;
import com.mealtoyou.userservice.application.dto.response.UserHomeResponseDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.application.service.JwtTokenProvider;
import com.mealtoyou.userservice.application.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    private long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

    // FIXME: ResponseEntity 로 반환
    @GetMapping("/profile")
    public Mono<UserInfoResponseDto> getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(getUserId(token));
    }

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<UserInfoResponseDto> updateUserProfile(
        @RequestHeader("Authorization") String token,
        @RequestPart(value = "userInfoRequestDto",required = false) UserInfoRequestDto userInfoRequestDto,
        @RequestPart(value = "image",required = false) FilePart image) {
        return userService.updateUserProfile(getUserId(token), image, userInfoRequestDto);
    }

    @PutMapping("/goal")
    public Mono<ResponseEntity<Void>> updateGoal(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid UserGoalRequestDto requestDto
    ) {
        return userService.updateGoal(getUserId(token), requestDto)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PutMapping("/weight")
    public Mono<ResponseEntity<Void>> updateWeight(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid UserWeightRequestDto requestDto
    ) {
        return userService.updateWeight(getUserId(token), requestDto)
            .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PutMapping("/inbody")
    public Mono<ResponseEntity<Object>> updateInbody(
        @RequestHeader("Authorization") String token,
        @RequestBody @Valid UserInbodyRequestDto requestDto
    ) {
        return userService.updateInbody(getUserId(token), token, requestDto)
            .then(Mono.just(ResponseEntity.ok().build()))
            .onErrorResume(e -> {
                log.error("체성분 정보 업데이트 중 에러 발생", e);
                return Mono.just(ResponseEntity.badRequest().build());
            });
    }

  @GetMapping("/health")
  public Mono<HealthInfoResponseDto> getHealthInfo(
      @RequestHeader("Authorization") String token) {
    return userService.getHealthInfo(getUserId(token));
  }

    @GetMapping("/home")
    public Mono<ResponseEntity<UserHomeResponseDto>> getUserHomeInfo(
        @RequestHeader("Authorization") String token
    ) {
        return userService.getUserHome(getUserId(token)).map(ResponseEntity::ok);
    }

}
