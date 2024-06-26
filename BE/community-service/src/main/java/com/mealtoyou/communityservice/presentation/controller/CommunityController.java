package com.mealtoyou.communityservice.presentation.controller;

import com.mealtoyou.communityservice.application.service.CommunityService;
import com.mealtoyou.communityservice.application.service.JwtTokenProvider;
import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.presentation.request.CreateCommunityRequest;
import com.mealtoyou.communityservice.presentation.request.UpdateCommunityRequest;
import com.mealtoyou.communityservice.presentation.response.ChattingUserInfoResponse;
import com.mealtoyou.communityservice.presentation.response.CommunityResponse;
import com.mealtoyou.communityservice.presentation.response.UserCommunityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CommunityController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CommunityService communityService;

    // 커뮤니티 생성
    @PostMapping("/communities")
    public Mono<Community> createCommunity(@RequestHeader("Authorization") String token,
                                           @Valid @RequestBody CreateCommunityRequest createCommunityRequest) {
        return communityService.createCommunity(createCommunityRequest, getUserId(token));
    }

    // 커뮤니티 조회
    @GetMapping("/communities/my")
    public Mono<UserCommunityResponse> getUserCommunityInfo(@RequestHeader("Authorization") String token) {
        return communityService.getUserCommunityInfo(getUserId(token));
    }

    // 커뮤니티 수정
    @PutMapping("/communities")
    public Mono<Community> updateCommunity(@RequestHeader("Authorization") String token,
                                           @Valid @RequestBody UpdateCommunityRequest updateCommunityRequest) {
        return communityService.updateCommunity(updateCommunityRequest, getUserId(token));
    }

    // 전체 그룹 조회
    @GetMapping("/communities")
    public Flux<CommunityResponse> getCommunityList(@RequestParam int page, @RequestParam int size) {
        return communityService.getCommunityList(page, size);
    }

    @PostMapping("/communities/goals")
    public Mono<String> dailyGoalCheck(@RequestHeader("Authorization") String token, @RequestParam int steps, @RequestParam int caloriesBurned) {
        return communityService.dailyGoalCheck(getUserId(token), steps, caloriesBurned);
    }

    @GetMapping("/communities/{communityId}")
    public Flux<ChattingUserInfoResponse> getChattingUserInfo(@PathVariable Long communityId) {
        return communityService.getChattingUserInfo(communityId);
    }

    @GetMapping("/communities/users/{userId}")
    public Flux<ChattingUserInfoResponse> getChattingUserInfoOne(@PathVariable Long userId) {
        return communityService.getChattingUserInfoOne(userId);
    }

    // 커뮤니티 가입
    @PostMapping("/communities/{communityId}")
    public Mono<String> registerCommunity(@RequestHeader("Authorization") String token, @PathVariable Long communityId) {
        return communityService.registerCommunity(getUserId(token), communityId);
    }

    // 커뮤니티에 가입되어 있는지 확인
    @GetMapping("/communities/check")
    public Mono<String> checkStatus(@RequestHeader("Authorization") String token) {
        return communityService.checkStatus(getUserId(token));
    }

    public Long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

}
