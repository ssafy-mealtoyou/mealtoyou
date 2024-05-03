package com.mealtoyou.userservice.presentation.controller;

import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.response.UserInfoResponseDto;
import com.mealtoyou.userservice.application.service.JwtTokenProvider;
import com.mealtoyou.userservice.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    private long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

    @GetMapping("/users/profile")
    public Mono<UserInfoResponseDto> getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(getUserId(token));
    }

    @PutMapping("/users/profile")
    public Mono<UserInfoResponseDto> updateUserProfile(
        @RequestHeader("Authorization") String token,
        @RequestBody UserInfoRequestDto userInfoRequestDto) {
        return userService.updateUserProfile(getUserId(token), userInfoRequestDto);
    }

}
