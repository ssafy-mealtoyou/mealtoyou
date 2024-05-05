package com.mealtoyou.userservice.presentation.controller;

import com.mealtoyou.userservice.application.dto.request.UserGoalRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInbodyRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserInfoRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserIntermittentFastingRequestDto;
import com.mealtoyou.userservice.application.dto.request.UserWeightRequestDto;
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
@RequestMapping("/api/users")
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    private long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

    @GetMapping("/profile")
    public Mono<UserInfoResponseDto> getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(getUserId(token));
    }

    @PutMapping("/profile")
    public Mono<UserInfoResponseDto> updateUserProfile(
        @RequestHeader("Authorization") String token,
        @RequestBody UserInfoRequestDto userInfoRequestDto) {
        return userService.updateUserProfile(getUserId(token), userInfoRequestDto);
    }

    @PutMapping("/fasting")
    public void updateFasting(
        @RequestHeader("Authorization") String token,
        @RequestBody UserIntermittentFastingRequestDto requestDto
        ) {
        userService.updateFasting(getUserId(token), requestDto);
    }

    @PutMapping("/goal")
    public void updateGoal(
        @RequestHeader("Authorization") String token,
        @RequestBody UserGoalRequestDto requestDto
    ) {
        userService.updateGoal(getUserId(token), requestDto);
    }

    @PutMapping("/weight")
    public void updateWeight(
        @RequestHeader("Authorization") String token,
        @RequestBody UserWeightRequestDto requestDto
    ) {
        userService.updateWeight(getUserId(token), requestDto);
    }

    @PutMapping("/inbody")
    public void updateInbody(
        @RequestHeader("Authorization") String token,
        @RequestBody UserInbodyRequestDto requestDto
    ) {
        userService.updateInbody(getUserId(token), requestDto);
    }


}
