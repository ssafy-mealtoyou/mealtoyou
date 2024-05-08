package com.mealtoyou.communityservice.presentation.controller;

import com.mealtoyou.communityservice.application.service.CommunityService;
import com.mealtoyou.communityservice.application.service.JwtTokenProvider;
import com.mealtoyou.communityservice.domain.model.Community;
import com.mealtoyou.communityservice.presentation.request.CreateCommunityRequest;
import com.mealtoyou.communityservice.presentation.request.UpdateCommunityRequest;
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
        Long userId = jwtTokenProvider.getUserId(token);
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
    public Flux<CommunityResponse> getCommunityList(int page, int size) {
        return communityService.getCommunityList(page, size);
    }

    @PostMapping("/communities/goals")
    public Mono<String> dailyGoalCheck(@RequestHeader("Authorization") String token, int steps, int caloriesBurned) {
        return communityService.dailyGoalCheck(getUserId(token), steps, caloriesBurned);
    }

    public Long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

}
